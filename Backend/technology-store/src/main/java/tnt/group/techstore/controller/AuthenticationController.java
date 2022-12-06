package tnt.group.techstore.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tnt.group.techstore.config.JwtUtils;
import tnt.group.techstore.model.Account;
import tnt.group.techstore.model.AccountDetail;
import tnt.group.techstore.model.Customer;
import tnt.group.techstore.payload.request.LoginRequest;
import tnt.group.techstore.payload.request.SignupRequest;
import tnt.group.techstore.payload.response.AccountInfoResponse;
import tnt.group.techstore.payload.response.MessageResponse;
import tnt.group.techstore.repository.AccountRepository;
import tnt.group.techstore.repository.CustomerRepository;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAccount(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AccountDetail accountDetails = (AccountDetail) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(accountDetails);
        String jwt = jwtUtils.generateJwtToken(authentication);
        List<String> roles = accountDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new AccountInfoResponse(
                        jwt,
                        accountDetails.getId(),
                        accountDetails.getUsername(),
                        roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerAccount(@Valid @RequestBody SignupRequest signUpRequest) {
        if (accountRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

//     Create new account's account
        Account account = new Account();
        account.setUsername(signUpRequest.getUsername());
        account.setPassword(encoder.encode(signUpRequest.getPassword()));
        account.setStatus("Active");
        account.setRole("user");
        accountRepository.save(account);
        Customer cus = new Customer();
        cus.setType(0);
        cus.setStatus("Active");
        cus.setAccount(account);
        customerRepository.save(cus);
        return ResponseEntity.ok(new MessageResponse("Account registered successfully!"));
    }
    @PostMapping("/signout")
    public ResponseEntity<?> logoutAccount() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}
