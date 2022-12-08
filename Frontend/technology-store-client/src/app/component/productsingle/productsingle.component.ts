import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../service/product/product.service';
@Component({
  selector: 'app-productsingle',
  templateUrl: './productsingle.component.html',
  styleUrls: ['./productsingle.component.css'],
})
export class ProductsingleComponent implements OnInit {
  product: any = {};
  errorMessage = '';
  pros: any[] = [];
  constructor(
    private productService: ProductService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProductByID(id).subscribe({
      next: (data) => {
        this.product = data;
        console.log(data);
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = err.error.message;
      },
    });
  }

  addToCart(product: any) {
    if (!this.productService.productInCart(product)) {
      //product.quantity = 1;
      this.productService.addToCart(product);
      this.pros = [...this.productService.getProduct()];
      alert('Add to cart successful!');
      //this.subTotal = product.price;
    }
  }
}
