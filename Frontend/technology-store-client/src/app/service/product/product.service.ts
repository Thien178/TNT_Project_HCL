import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
const httpOptions: any = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};
const API_URL = 'http://localhost:8094/api/product/';
@Injectable({
  providedIn: 'root',
})
export class ProductService {
  pros: any[] = [];
  constructor(private http: HttpClient) {}

  getAllProduct(): Observable<any> {
    return this.http.get(API_URL + 'auth/viewall');
  }

  getProductByID(id: number): Observable<any> {
    return this.http.get(API_URL +'auth/' + `${id}`);
  }

  deleteProductByID(id: number): Observable<any> {
    return this.http.delete(API_URL  +'auth/' +`${id}`);
  }

  createByCategory(
    name: string,
    price: number,
    image: string,
    amount: string,
    status: string,
    description: string,
    saleOff: string,
    id: number
  ): Observable<any> {
    return this.http.post(
      API_URL + 'createByCategory/' + `${id}`,
      {
        name,
        price,
        image,
        amount,
        status,
        description,
        saleOff,
      },
      httpOptions
    );
  }

  updateProduct(
    name: string,
    price: number,
    image: string,
    amount: string,
    status: string,
    description: string,
    saleOff: string,
    id: number
  ): Observable<any> {
    return this.http.put(
      API_URL + `${id}`,
      {
        name,
        price,
        image,
        amount,
        status,
        description,
        saleOff,
      },
      httpOptions
    );
  }

  saveCart(): void {
    localStorage.setItem('cart_items', JSON.stringify(this.pros));
  }

  addToCart(addedProduct: any) {
    this.pros.push(addedProduct);
    this.saveCart();
  }
  getProduct() {
    return this.pros;
  }
  loadCart(): void {
    this.pros = JSON.parse(localStorage.getItem('cart_items') as any) || [];
  }

  productInCart(product: any): boolean {
    return this.pros.findIndex((x: any) => x.id === product.id) > -1;
  }

  removeProduct(product: any) {
    const index = this.pros.findIndex((x: any) => x.id === product.id);

    if (index > -1) {
      this.pros.splice(index, 1);
      this.saveCart();
    }
  }

  clearProducts() {
    localStorage.clear();
  }
}
