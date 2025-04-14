import { Component, OnInit } from '@angular/core';
import { ApplicationService } from 'src/app/services/application.service';
import { Application } from 'src/app/models/application.model';
import { InternshipOffer } from 'src/app/models/internship-offer.model';
import { Router } from '@angular/router';
import { InternshipOfferService } from 'src/app/services/internship-offer.service';

@Component({
  selector: 'app-dashboard-applications',
  templateUrl: './dashboard-applications.component.html',
  styleUrls: ['./dashboard-applications.component.css']
})
export class DashboardApplicationsComponent implements OnInit {
  applications: Application[] = [];
  filteredApplications: Application[] = [];
  internshipOffers: InternshipOffer[] = [];
  notifications: any[] = [];
  searchTerm: string = '';
  offerIdFilter: number | '' = '';
  sortField: keyof Application | 'internshipOffer' = 'id';
  sortDirection: 'asc' | 'desc' = 'asc';

  constructor(
    private internshipOfferService: InternshipOfferService,
    private applicationService: ApplicationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.fetchApplications();
    this.fetchNotifications();
    this.fetchOffers();
  }

  fetchApplications(): void {
    this.internshipOfferService.getAllInternshipOffersWithWeather().subscribe(
      (offers) => {
        this.internshipOffers = offers;
        this.filterApplications();
      },
      (error) => {
        console.error('❌ Error fetching internship offers:', error);
        alert('An error occurred while fetching internship offers.');
      }
    );
  }
  

  fetchOffers(): void {
    this.applicationService.getAllApplications().subscribe(
      (applications) => {
        this.applications = applications.map(app => ({
          ...app,
          status: app.status || 'WAITING'
        }));
        this.filterApplications();
      },
      (error) => {
        console.error('❌ Error fetching applications:', error);
        alert('An error occurred while fetching applications.');
      }
    );
  }

  fetchNotifications(): void {
    this.applicationService.getAllNotifications().subscribe(
      (notifications) => {
        this.notifications = notifications;
      },
      (error) => {
        console.error('❌ Error fetching notifications:', error);
      }
    );
  }

  filterApplications(): void {
    let tempApplications = [...this.applications];

    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      tempApplications = tempApplications.filter(application =>
        application.fullName?.toLowerCase().includes(term) ||
        application.email?.toLowerCase().includes(term)
      );
    }

    if (this.offerIdFilter !== '') {
      tempApplications = tempApplications.filter(application =>
        application.internshipOffer?.id === Number(this.offerIdFilter)
      );
    }

    if (this.sortField) {
      tempApplications.sort((a, b) => {
        let aValue: any = a[this.sortField];
        let bValue: any = b[this.sortField];

        if (typeof aValue === 'string' && typeof bValue === 'string') {
          return this.sortDirection === 'asc' ? aValue.localeCompare(bValue) : bValue.localeCompare(aValue);
        }
        return this.sortDirection === 'asc' ? aValue - bValue : bValue - aValue;
      });
    }

    this.filteredApplications = tempApplications;
  }

  sort(field: keyof Application | 'internshipOffer'): void {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.filterApplications();
  }

  deleteApplication(id: number): void {
    if (confirm('🚨 Are you sure you want to delete this application?')) {
      this.applicationService.deleteApplication(id).subscribe(() => {
        this.applications = this.applications.filter(app => app.id !== id);
        this.filterApplications();
      });
    }
  }

  // ✅ Méthode pour changer le statut de la candidature
  changeStatus(application: Application, newStatus: 'ACCEPTED' | 'WAITING' | 'DENIED'): void {
    application.status = newStatus; // ✅ Mise à jour immédiate dans l'UI

    this.applicationService.updateApplicationStatus(application.id, newStatus).subscribe(
      () => {
        console.log(`✅ Status updated to ${newStatus} for application ID ${application.id}`);
        this.refreshStatus(application.id, newStatus);
      },
      (error) => {
        console.error('❌ Error updating status:', error);
        alert('An error occurred while updating the status.');
      }
    );
  }

  // ✅ Rafraîchir l'affichage après mise à jour du statut
  private refreshStatus(applicationId: number, newStatus: 'ACCEPTED' | 'WAITING' | 'DENIED'): void {
    const index = this.applications.findIndex(app => app.id === applicationId);
    if (index !== -1) {
        this.applications[index].status = newStatus;
        this.filterApplications();
    }
  }
} // ✅ FIN de la classe (Ne PAS ajouter d'accolade supplémentaire après)
