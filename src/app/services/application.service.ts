import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Application } from '../models/application.model';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
  private apiUrl = 'http://localhost:8888/applications'; // Adjust this to match your Spring Boot backend URL
  private notificationsUrl = 'http://localhost:8888/notifications'; // Notifications endpoint

  constructor(private http: HttpClient) {}

  getAllApplications(): Observable<Application[]> {
    return this.http.get<Application[]>(this.apiUrl);
  }
  updateApplicationStatus(id: number, status: 'ACCEPTED' | 'WAITING' | 'DENIED'): Observable<Application> {
    return this.http.put<Application>(`${this.apiUrl}/${id}/status`, { status });
  }
  

  getApplicationById(id: number): Observable<Application> {
    return this.http.get<Application>(`${this.apiUrl}/${id}`);
  }

  createApplication(application: Application, id: number): Observable<Application> {
    return this.http.post<Application>(`${this.apiUrl}/${id}`, application);
  }

  updateApplication(id: number, application: Application): Observable<Application> {
    return this.http.put<Application>(`${this.apiUrl}/${id}`, application);
  }

  deleteApplication(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getApplicationsByOfferId(offerId: number): Observable<Application[]> {
    return this.http.get<Application[]>(`${this.apiUrl}/offer/${offerId}`);
  }

  getApplicationsByUserId(userId: number): Observable<Application[]> {
    return this.http.get<Application[]>(`${this.apiUrl}/user/${userId}`);
  }

  getAllNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(this.notificationsUrl);
  }

  /**
   * ✅ Check if a user has already applied for a specific internship offer
   * @param email User's email
   * @param offerId Internship Offer ID
   * @returns Observable<boolean> (true if already applied, false otherwise)
   */
  hasApplied(email: string, offerId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/has-applied?email=${email}&offerId=${offerId}`);
  }
}
