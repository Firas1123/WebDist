import { Component, OnInit } from '@angular/core';
import { MyApplicationsService } from 'src/app/services/myapplications.service';  
import { Application } from 'src/app/models/application.model';

@Component({
  selector: 'app-my-applications',
  templateUrl: './my-applications.component.html',
  styleUrls: ['./my-applications.component.css']
})
export class MyApplicationsComponent implements OnInit {
  applications: Application[] = [];
  filteredApplications: Application[] = [];
  userEmail = "nour.benmna@esprit.tn"; // ⚠️ Remplacer par une valeur dynamique plus tard
  displayedColumns: string[] = ['title', 'company', 'location', 'duration', 'stageType', 'offerStatus', 'applicationStatus', 'submissionDate', 'image'];
  loading: boolean = true;

  constructor(private myApplicationsService: MyApplicationsService) {}

  ngOnInit(): void {
    this.loading = true;
    this.fetchUserApplications();
  }

  // 🔄 Récupérer les candidatures de l'utilisateur connecté
  fetchUserApplications(): void {
    this.myApplicationsService.getApplicationsByUser(this.userEmail).subscribe(
      (data) => {
        console.log("📥 Données récupérées :", data);
        this.applications = data.map(app => ({
          ...app,
          status: app.status || 'WAITING' // ✅ Statut par défaut si absent
        }));
        this.filteredApplications = [...this.applications]; // 🔄 Cloner les données pour le filtre
        this.loading = false;
      },
      (error) => {
        console.error("❌ Erreur lors de la récupération des candidatures", error);
        this.loading = false;
      }
    );
  }

  // 🔎 Appliquer un filtre en fonction de la recherche utilisateur
  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
    
    if (!filterValue) {
      this.filteredApplications = [...this.applications]; // 🔄 Restaurer la liste complète si vide
      return;
    }

    this.filteredApplications = this.applications.filter(app =>
      app.internshipOffer?.title?.toLowerCase().includes(filterValue) ||
      app.internshipOffer?.companyName?.toLowerCase().includes(filterValue) ||
      app.internshipOffer?.location?.toLowerCase().includes(filterValue)
    );
  }

  // ✅ Style des statuts de l'offre (OPEN, CLOSED, ARCHIVED)
  getStatusClass(status: string): string {
    switch (status) {
      case 'OPEN': return 'badge bg-primary';
      case 'CLOSED': return 'badge bg-secondary';
      case 'ARCHIVED': return 'badge bg-dark';
      default: return 'badge bg-light';
    }
  }

  // ✅ Affichage du statut de candidature (ACCEPTED, WAITING, DENIED)
  getApplicationStatusClass(status: string): string {
    switch (status) {
      case 'ACCEPTED': return 'badge bg-success';  // 🟢 Vert pour accepté
      case 'DENIED': return 'badge bg-danger';     // 🔴 Rouge pour refusé
      case 'WAITING': return 'badge bg-warning text-dark'; // 🟠 Orange pour en attente
      default: return 'badge bg-secondary';
    }
  }

  // 🔎 Ouvrir une image dans un nouvel onglet
  openImageModal(imageUrl: string): void {
    window.open(imageUrl, '_blank');
  }
}
