import { Component, OnInit, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InternshipOfferService } from '../../services/internship-offer.service';
import { ActivatedRoute, Router } from '@angular/router';
import { InternshipOffer } from 'src/app/models/internship-offer.model';
import * as L from 'leaflet';
import { Modal } from 'bootstrap'; // Import Bootstrap Modal

@Component({
  selector: 'app-internship-offer-form',
  templateUrl: './internship-offer-form.component.html',
  styleUrls: ['./internship-offer-form.component.css']
})
export class InternshipOfferFormComponent implements OnInit, AfterViewInit {
  internshipForm: FormGroup;
  selectedFiles: File[] = [];
  fileError = false;
  offerId: number | null = null;
  map: L.Map | null = null;
  geocodeError: string | null = null;

  @ViewChild('mapContainer', { static: true }) mapContainer!: ElementRef;

  locations: string[] = [
    "Ariana", "Béja", "Ben Arous", "Bizerte", "Gabès", "Gafsa",
    "Jendouba", "Kairouan", "Kasserine", "Kébili", "Kef", "Mahdia",
    "Manouba", "Médenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid",
    "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"
  ];

  constructor(
    private fb: FormBuilder,
    private offerService: InternshipOfferService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.internshipForm = this.fb.group({
      title: ['', [Validators.required, Validators.maxLength(100)]],
      companyName: ['', [Validators.required, Validators.maxLength(100)]],
      location: ['', Validators.required],
      duration: ['', [Validators.required, Validators.min(1)]],
      stageType: ['', Validators.required],
      offerStatus: ['', Validators.required],
      isRemote: [false],
      isPaid: [false],
      startDate: ['', Validators.required],
      jobDescription: ['', [Validators.required, Validators.maxLength(500)]]
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params['id']) {
        this.offerId = +params['id'];
        this.loadOffer(this.offerId);
      }
    });
  }

  ngAfterViewInit(): void {
    this.initializeMap();
  }

  initializeMap(): void {
    if (this.mapContainer && this.mapContainer.nativeElement) {
      this.map = L.map(this.mapContainer.nativeElement, {
        zoom: 6,
        center: L.latLng(34.0, 9.0), // Center on Tunisia
        maxBounds: L.latLngBounds(L.latLng(30.0, 7.0), L.latLng(38.0, 12.0)),
        maxBoundsViscosity: 1.0
      });

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        attribution: '© OpenStreetMap contributors'
      }).addTo(this.map);

      this.map.on('click', (e: L.LeafletMouseEvent) => {
        this.geocodeError = null;
        this.getLocationFromCoordinates(e.latlng.lat, e.latlng.lng);

        if (this.map) {
          this.map.eachLayer((layer) => {
            if (layer instanceof L.Marker) {
              this.map?.removeLayer(layer);
            }
          });
          L.marker(e.latlng).addTo(this.map);
        }
      });
    }
  }

  async getLocationFromCoordinates(lat: number, lng: number) {
    try {
      const response = await fetch(
        `https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}&zoom=10&addressdetails=1`
      );
      const data = await response.json();
      const governorate = data.address.state || data.address.city || data.address.town || null;

      if (!governorate) {
        throw new Error('No recognizable location found');
      }

      const matchedGovernorate = this.locations.find(loc => loc.toLowerCase() === governorate.toLowerCase()) || governorate;
      this.internshipForm.patchValue({ location: matchedGovernorate });
    } catch (error) {
      console.error('Error fetching location:', error);
      this.geocodeError = 'Could not determine location. Please try again.';
      this.internshipForm.patchValue({ location: '' });
    }
  }

  async geocodeLocationToCoordinates(location: string) {
    try {
      const response = await fetch(
        `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(location + ', Tunisia')}&limit=1`
      );
      const data = await response.json();
      if (data.length > 0) {
        const { lat, lon } = data[0];
        const latLng = L.latLng(parseFloat(lat), parseFloat(lon));
        if (this.map) {
          this.map.setView(latLng, 10);
          this.map.eachLayer((layer) => {
            if (layer instanceof L.Marker) {
              this.map?.removeLayer(layer);
            }
          });
          L.marker(latLng).addTo(this.map);
        }
      }
    } catch (error) {
      console.error('Error geocoding location:', error);
    }
  }

  loadOffer(id: number): void {
    this.offerService.getInternshipOfferById(id).subscribe(
      (offer: InternshipOffer) => {
        this.internshipForm.patchValue(offer);
        if (offer.location && this.map) {
          this.geocodeLocationToCoordinates(offer.location);
        }
      },
      (error) => {
        console.error('❌ Error loading offer:', error);
      }
    );
  }

  onFileSelected(event: any) {
    this.selectedFiles = Array.from(event.target.files);
    this.fileError = this.selectedFiles.length === 0;
  }

  submitOffer() {
    if (this.internshipForm.invalid) {
      this.internshipForm.markAllAsTouched();
      return;
    }

    const formData = { ...this.internshipForm.value };

    if (this.offerId) {
      this.offerService.updateInternshipOffer(this.offerId, formData, this.selectedFiles).subscribe(
        () => {
          alert('✅ Offer updated successfully!');
          this.router.navigate(['/backoffice/dashboard-offers']);
        },
        (error) => {
          this.handleError(error);
        }
      );
    } else {
      if (this.selectedFiles.length === 0) {
        this.fileError = true;
        return;
      }

      this.offerService.addInternshipOffer(formData, this.selectedFiles).subscribe(
        () => {
          alert('✅ Offer added successfully!');
          this.router.navigate(['/backoffice/dashboard-offers']);
        },
        (error) => {
          this.handleError(error);
        }
      );
    }
  }

  private handleError(error: any) {
    if (error.status === 400 && error.error?.error === 'Vous avez déjà postulé à cette offre.') {
      this.showAlreadySubmittedModal();
    } else {
      alert('❌ Error processing offer!');
      console.error('Error:', error);
    }
  }

  private showAlreadySubmittedModal() {
    const modalElement = document.getElementById('alreadySubmittedModal');
    if (modalElement) {
      const modal = new Modal(modalElement);
      modal.show();
    }
  }
}