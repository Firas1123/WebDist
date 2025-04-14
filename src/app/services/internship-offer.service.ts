import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InternshipOfferService {
  private apiUrl = 'http://localhost:8888/internship-offers'; // URL du backend

  constructor(private http: HttpClient) {}


  addInternshipOffer(offer: any, files?: File[]): Observable<any> {
    const formData = new FormData();

    // Ajouter les champs textuels
    formData.append('title', offer.title);
    formData.append('companyName', offer.companyName);
    formData.append('location', offer.location);
    formData.append('duration', offer.duration.toString());
    formData.append('stageType', offer.stageType.toUpperCase());
    formData.append('offerStatus', offer.offerStatus.toUpperCase());

formData.append('remote', offer.isRemote ? 'true' : 'false');
formData.append('paid', offer.isPaid ? 'true' : 'false');

    formData.append('startDate', offer.startDate);
    formData.append('jobDescription', offer.jobDescription);

  
    if (files && files.length > 0) {
      files.forEach(file => {
        formData.append('files', file);
      });
    }

    return this.http.post(`${this.apiUrl}/add`, formData, { observe: 'response' });

  }


// ✅ Nouvelle méthode pour récupérer les offres avec météo
getAllInternshipOffersWithWeather(): Observable<any[]> {
  return this.http.get<any[]>(`${this.apiUrl}/with-weather`);
}



  getInternshipOfferById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }
// ✅ Pour ceux qui veulent les offres sans météo
getAllInternshipOffers(): Observable<any[]> {
  return this.http.get<any[]>(`${this.apiUrl}/all`);
}

  
  updateInternshipOffer(id: number, updatedOffer: any, files?: File[]): Observable<any> {
    const formData = new FormData();

   
    formData.append('title', updatedOffer.title);
    formData.append('companyName', updatedOffer.companyName);
    formData.append('location', updatedOffer.location);
    formData.append('duration', updatedOffer.duration.toString());
    formData.append('stageType', updatedOffer.stageType.toUpperCase());
    formData.append('offerStatus', updatedOffer.offerStatus.toUpperCase());

formData.append('remote', updatedOffer.isRemote ? 'true' : 'false');
formData.append('paid', updatedOffer.isPaid ? 'true' : 'false');

    formData.append('startDate', updatedOffer.startDate);
    formData.append('jobDescription', updatedOffer.jobDescription);

   
    if (files && files.length > 0) {
      files.forEach(file => {
        formData.append('files', file);
      });
    }

    return this.http.put(`${this.apiUrl}/update/${id}`, formData);
  }


  deleteInternshipOffer(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`);
  }

 
  searchInternshipOffersByTitle(title: string): Observable<any[]> {
    let params = new HttpParams().set('title', title);
    return this.http.get<any[]>(`${this.apiUrl}/search/title`, { params });
  }


  searchInternshipOffersByStatus(status: string): Observable<any[]> {
    let params = new HttpParams().set('status', status.toUpperCase()); // Convertir en majuscules
    return this.http.get<any[]>(`${this.apiUrl}/search/status`, { params });
  }
}
