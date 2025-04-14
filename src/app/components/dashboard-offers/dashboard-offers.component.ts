import { Component, OnInit } from '@angular/core';
import { InternshipOffer } from 'src/app/models/internship-offer.model';


import { InternshipOfferService } from 'src/app/services/internship-offer.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard-offers',
  templateUrl: './dashboard-offers.component.html',
  styleUrls: ['./dashboard-offers.component.css']
})
export class DashboardOffersComponent implements OnInit {
  internshipOffers: InternshipOffer[] = [];
  filteredOffers: InternshipOffer[] = [];
  searchTerm: string = '';
  statusFilter: string = '';
  remoteFilter: boolean = false;
  paidFilter: boolean = false;
  sortField: keyof InternshipOffer = '' as keyof InternshipOffer;
  sortDirection: 'asc' | 'desc' = 'asc';

  constructor(
    private internshipService: InternshipOfferService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.fetchInternshipOffers();
  }

  getExpirationDate(creationDate: string): Date {
    const creation = new Date(creationDate);
    creation.setDate(creation.getDate() + 7);
    return creation;
  }


fetchInternshipOffers(): void {
    this.internshipService.getAllInternshipOffersWithWeather().subscribe(
      (offers: InternshipOffer[]) => {
        this.internshipOffers = offers;
        this.filterOffers();
      },
      (error: any) => {
        console.error('❌ Error fetching internship offers:', error);
      }
    );
  }
  

  filterOffers(): void {
    let tempOffers = [...this.internshipOffers];

    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      tempOffers = tempOffers.filter(offer =>
        offer.title.toLowerCase().includes(term) ||
        offer.companyName.toLowerCase().includes(term)
      );
    }

    if (this.statusFilter) {
      tempOffers = tempOffers.filter(offer => offer.offerStatus === this.statusFilter);
    }

    if (this.remoteFilter) {
      tempOffers = tempOffers.filter(offer => offer.remote);
    }

    if (this.paidFilter) {
      tempOffers = tempOffers.filter(offer => offer.paid);
    }

    if (this.sortField) {
      tempOffers.sort((a, b) => {
        const aValue = a[this.sortField];
        const bValue = b[this.sortField];

        if (typeof aValue === 'string' && typeof bValue === 'string') {
          return this.sortDirection === 'asc'
            ? aValue.localeCompare(bValue)
            : bValue.localeCompare(aValue);
        }

        if (typeof aValue === 'number' && typeof bValue === 'number') {
          return this.sortDirection === 'asc'
            ? aValue - bValue
            : bValue - aValue;
        }

        if (typeof aValue === 'boolean' && typeof bValue === 'boolean') {
          return this.sortDirection === 'asc'
            ? aValue === bValue ? 0 : aValue ? -1 : 1
            : bValue === aValue ? 0 : bValue ? -1 : 1;
        }

        return 0;
      });
    }

    this.filteredOffers = tempOffers;
  }

  sort(field: keyof InternshipOffer): void {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.filterOffers();
  }

  deleteOffer(id: number): void {
    if (confirm('Are you sure you want to delete this offer?')) {
      this.internshipService.deleteInternshipOffer(id).subscribe({
        next: () => {
          alert('✅ Offer deleted!');
          this.internshipOffers = this.internshipOffers.filter(offer => offer.id !== id);
          this.filterOffers();
        },
        error: (error) => {
          console.error('❌ Error deleting offer:', error);
          alert('❌ Error deleting offer.');
        }
      });
    }
  }

  updateOffer(id: number): void {
    this.router.navigate(['/backoffice/internship-offer-form'], {
      queryParams: { id: id }
    });
  }

  clearSearch(searchBox: HTMLInputElement): void {
    this.searchTerm = '';
    searchBox.value = '';
    this.filterOffers();
  }
}
