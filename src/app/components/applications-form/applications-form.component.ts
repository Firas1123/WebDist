import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApplicationService } from '../../services/application.service';
import { ActivatedRoute, Router } from '@angular/router';
import { InternshipOfferService } from 'src/app/services/internship-offer.service';
import { InternshipOffer } from 'src/app/models/internship-offer.model';

@Component({
  selector: 'app-applications-form',
  templateUrl: './applications-form.component.html',
  styleUrls: ['./applications-form.component.css']
})
export class ApplicationFormComponent implements OnInit {
  applicationForm: FormGroup;
  currentDate: string = new Date().toISOString().slice(0, 16);
  offer: InternshipOffer | null = null;
  private offerId: number | undefined;
  hasAlreadyApplied: boolean = false;

  constructor(
    private fb: FormBuilder,
    private applicationService: ApplicationService,
    private router: Router,
    private route: ActivatedRoute,
    private internshipOfferService: InternshipOfferService
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
      submissionDate: [this.currentDate, Validators.required],
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.offerId = +params['offerId'];
      console.log('🔹 Offer ID:', this.offerId);

      if (this.offerId) {
        this.fetchInternshipOffer(this.offerId);
      }
    });
  }

  fetchInternshipOffer(offerId: number): void {
    this.internshipOfferService.getInternshipOfferById(offerId).subscribe(
      (offer) => {
        this.offer = offer;
        this.applicationForm.patchValue({ internshipOffer: offer });
        console.log('✅ Internship offer loaded successfully:', offer);
      },
      (error) => {
        console.error('❌ Error fetching offer:', error);
      }
    );
  }

  /**
   * Redirect to the home page after the popup OK button is clicked
   */
  redirectToHome(): void {
    this.router.navigate(['/']);
  }

  submitApplication(): void {
    if (this.applicationForm.invalid) {
      this.applicationForm.markAllAsTouched();
      return;
    }

    const applicationData = {
      ...this.applicationForm.value,
      offer_id: { id: this.offerId }
    };

    this.applicationService.createApplication(applicationData, this.offerId!).subscribe(
      (response) => {
        console.log('✅ Application submitted successfully:', response);
        alert('✅ Application submitted successfully!');
        this.router.navigate(['/backoffice/dashboard-apps']);
      },
      (error) => {
          console.log('✅ User has already applied, showing popup.');
      }
    );
  }
}