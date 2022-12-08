import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const API_URL = 'http://localhost:8094/api/category/';
@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  constructor(private http: HttpClient) {}

  getAllCategory(): Observable<any> {
    return this.http.get(API_URL + 'auth/viewall');
  }

  getCategoryByID(id: number): Observable<any> {
    return this.http.get(API_URL +'auth/' + `${id}`);
  }
}
