import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InternshipOfferService } from 'src/app/services/internship-offer.service';
import { Router } from '@angular/router';
import * as L from 'leaflet';
import { ApplicationService } from '../services/application.service';

interface Weather {
  description: string;
  icon: string;
}

interface WeatherResponse {
  name: string;
  main: { temp: number };
  weather: Weather[];
}

interface InternshipOffer {
  id?: number;
  title: string;
  companyName: string;
  location: string;
  stageType: string;
  offerStatus: string;
  paid: boolean;
  imageUrls: string[];
  jobDescription: string;
  lat?: number;
  lng?: number;
  weather?: WeatherResponse;
}

@Component({
  selector: 'app-internship-offers',
  templateUrl: './internship-offers.component.html',
  styleUrls: ['./internship-offers.component.css']
})
export class InternshipOffersComponent implements OnInit, AfterViewInit {
  offers: InternshipOffer[] = [];
  filteredOffers: InternshipOffer[] = [];
  searchTerm: string = '';
  statusFilter: string = '';
  paidFilter: boolean = false;
  sortField: keyof InternshipOffer = '' as keyof InternshipOffer;
  sortDirection: 'asc' | 'desc' = 'asc';
  proximityRadius: number = 50;
  selectedOffer: InternshipOffer | null = null;
  showApplicationForm: boolean = false;
  applicationForm: FormGroup;
  map!: L.Map;
  selectedLocation: { lat: number; lng: number; name: string } | null = null;
  geocodeError: string | null = null;
  hasAlreadyApplied: boolean = false;

  @ViewChild('mapFilter') mapFilter!: ElementRef;

  constructor(
    private offerService: InternshipOfferService,
    private applicationService: ApplicationService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.applicationForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.maxLength(100)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required]],
      city: ['', [Validators.required]],
      careerObjective: ['', [Validators.required, Validators.maxLength(1000)]],
      technologyProfile: ['', [Validators.required, Validators.maxLength(1000)]],
      skills: ['', [Validators.required]],
      experience: ['', [Validators.required, Validators.maxLength(2000)]],
      submissionDate: [new Date().toISOString().slice(0, 16), Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadOffers();
  }

  ngAfterViewInit(): void {
    if (!this.mapFilter || !this.mapFilter.nativeElement) {
      console.error('Map container not found in DOM');
      return;
    }
    this.initializeMap();
  }

  initializeMap(): void {
    console.log('Initializing map...');
    this.map = L.map(this.mapFilter.nativeElement, {
      zoom: 6,
      center: L.latLng(34.0, 9.0),
      maxBounds: L.latLngBounds(L.latLng(30.0, 7.0), L.latLng(38.0, 12.0)),
      maxBoundsViscosity: 1.0
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      attribution: '© OpenStreetMap contributors'
    }).addTo(this.map);

    setTimeout(() => {
      this.map.invalidateSize();
      console.log('Map size invalidated');
    }, 100);

    this.map.on('click', (e: L.LeafletMouseEvent) => {
      console.log('Map clicked at:', e.latlng);
      this.geocodeError = null;
      this.getLocationFromCoordinates(e.latlng.lat, e.latlng.lng);
      this.map.eachLayer((layer) => {
        if (layer instanceof L.Marker || layer instanceof L.Circle) {
          this.map.removeLayer(layer);
        }
      });
      L.circle([e.latlng.lat, e.latlng.lng], { radius: this.proximityRadius * 1000 }).addTo(this.map);
    });
  }

  async getLocationFromCoordinates(lat: number, lng: number) {
    try {
      const response = await fetch(
        `https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}&zoom=10&addressdetails=1`
      );
      const data = await response.json();
      const locationName = data.address.state || data.address.city || data.address.town || 'Unknown location';
      this.selectedLocation = { lat, lng, name: locationName };
      console.log('Selected location:', this.selectedLocation);
      this.filterOffers();
    } catch (error) {
      console.error('Error fetching location:', error);
      this.geocodeError = 'Could not determine location. Please try again.';
      this.selectedLocation = null;
      this.filterOffers();
    }
  }

  async geocodeLocation(location: string): Promise<{ lat: number; lng: number }> {
    try {
      const response = await fetch(
        `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(location)}&limit=1`
      );
      const data = await response.json();
      if (data.length > 0) {
        return { lat: parseFloat(data[0].lat), lng: parseFloat(data[0].lon) };
      }
      console.warn(`No coordinates found for location: ${location}`);
      return { lat: 0, lng: 0 };
    } catch (error) {
      console.error('Geocoding error:', error);
      return { lat: 0, lng: 0 };
    }
  }

  // ✅ Méthode modifiée pour charger les offres avec météo
  async loadOffers(): Promise<void> {
    this.offerService.getAllInternshipOffersWithWeather().subscribe(
      async (offers) => {
        console.log('📦 Données reçues du backend:', offers); // <= AJOUTE ÇA
    
        this.offers = offers;
        for (const offer of this.offers) {
          if (!offer.lat || !offer.lng) {
            const coords = await this.geocodeLocation(offer.location);
            offer.lat = coords.lat;
            offer.lng = coords.lng;
          }
        }
        this.filteredOffers = [...this.offers];
        this.filterOffers();
      },
      (error) => {
        console.error('❌ Erreur lors du chargement des offres:', error);
      }
    );
  }    

  calculateDistance(lat1: number, lon1: number, lat2: number, lon2: number): number {
    const R = 6371;
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
  }

  filterOffers(): void {
    let tempOffers = [...this.offers];

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

    if (this.paidFilter) {
      tempOffers = tempOffers.filter(offer => offer.paid);
    }

    if (this.selectedLocation && this.proximityRadius) {
      tempOffers = tempOffers.filter(offer => {
        if (offer.lat && offer.lng && this.selectedLocation) {
          const distance = this.calculateDistance(
            this.selectedLocation.lat,
            this.selectedLocation.lng,
            offer.lat,
            offer.lng
          );
          return distance <= this.proximityRadius;
        }
        return false;
      });
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

  onProximityChange(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.proximityRadius = value ? parseInt(value) : 50;
    this.filterOffers();
  }

  openDetails(offer: InternshipOffer): void { this.selectedOffer = offer; }
  closeDetails(): void { this.selectedOffer = null; }
  openApplicationForm(offer: InternshipOffer): void { this.selectedOffer = offer; this.showApplicationForm = true; this.hasAlreadyApplied = false; }
  closeApplicationForm(): void { this.showApplicationForm = false; this.applicationForm.reset({ submissionDate: new Date().toISOString().slice(0, 16) }); }
  closeAlreadyAppliedPopup(): void { this.hasAlreadyApplied = false; this.showApplicationForm = false; this.applicationForm.reset({ submissionDate: new Date().toISOString().slice(0, 16) }); }

  submitApplication(): void {
    if (this.applicationForm.invalid) {
      this.applicationForm.markAllAsTouched();
      return;
    }
    const applicationData = { ...this.applicationForm.value, offer_id: { id: this.selectedOffer?.id } };
    this.applicationService.createApplication(applicationData, this.selectedOffer!.id!).subscribe(
      (response: any) => {
        console.log('✅ Application submitted successfully:', response);
        alert('✅ Application submitted successfully!');
        this.closeApplicationForm();
      },
      (error: { status: number; error: { error: string; message: any; }; }) => {
        console.error('❌ Error submitting application:', error);
        if (error.status === 400 && error.error?.error === 'Vous avez déjà postulé à cette offre.') {
          this.hasAlreadyApplied = true;
          console.log('✅ User has already applied, showing popup.');
        } else {
          alert('❌ Error submitting application: ' + (error.error?.message || 'Already Submitted'));
          console.error('Detailed error:', error);
        }
      }
    );
  }
}
