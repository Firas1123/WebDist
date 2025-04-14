import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MyApplicationsService {  // ✅ Nom du service corrigé ici
  private apiUrl = 'http://localhost:8888/applications/email';

  constructor(private http: HttpClient) {}

  getApplicationsByUser(email: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${email}`);
  }
}
