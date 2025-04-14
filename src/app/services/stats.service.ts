import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface StatsDTO {
  totalOffers: number;
  openOffers: number;
  closedOffers: number;
  archivedOffers: number;
  paidOffers: number;
  remoteOffers: number;
  averageDuration: number;
  totalApplications: number;
  avgApplicationsPerOffer: number;
  totalNotifications: number;
}

@Injectable({
  providedIn: 'root'
})
export class StatsService {
  private apiUrl = 'http://localhost:8888/statistics/stats'; // Adjust based on backend

  constructor(private http: HttpClient) {}

  getStats(): Observable<StatsDTO> {
    return this.http.get<StatsDTO>(this.apiUrl);
  }
}
