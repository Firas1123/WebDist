import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
// ✅ Import des composants Front
import { HeaderComponent } from './components/header/header.component';
import { HomeComponent } from './components/home/home.component';
import { FooterComponent } from './components/footer/footer.component';
import { HeroComponent } from './components/hero/hero.component';
import { PartnershipsComponent } from './components/partnerships/partnerships.component';
import { AboutComponent } from './components/about/about.component';
import { TestimonialsComponent } from './components/testimonials/testimonials.component';
import { ContactComponent } from './components/contact/contact.component';

// ✅ Import des composants Back
import { FooterbackComponent } from './components/footerback/footerback.component';
import { HomebackComponent } from './components/homeback/homeback.component';
import { SidebackComponent } from './components/sideback/sideback.component';
import { HeaderbackComponent } from './components/headerback/headerback.component';
import { ProfilebackComponent } from './profileback/profileback.component';
import { LayoutComponent } from './components/layout/layout.component';

// ✅ Import des pages importantes
import { LoginfrontComponent } from './loginfront/loginfront.component';
import { InternshipOfferFormComponent } from './components/internship-offer-form/internship-offer-form.component';
import { DashboardOffersComponent } from './components/dashboard-offers/dashboard-offers.component';


// ✅ Modules nécessaires pour Angular
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { InternshipOffersComponent } from './intershipoffers/internship-offers.component';
import { DashboardApplicationsComponent } from './components/dashboard-applications/dashboard-applications.component';
import { ApplicationFormComponent } from './components/applications-form/applications-form.component';
import { MyApplicationsComponent } from './components/my-applications/my-applications.component';
import { NgChartsModule } from 'ng2-charts';
import { GlobalStatisticsComponent } from './statistics/global-statistics/global-statistics.component';
import { CompanyStatisticsComponent } from './statistics/company-statistics/company-statistics.component';
import { StatisticsService } from './services/statistics.service';



@NgModule({
  declarations: [
    AppComponent,
    // ✅ Composants Front
    HeaderComponent,
    HomeComponent,
    FooterComponent,
    HeroComponent,
    PartnershipsComponent,
    AboutComponent,
    TestimonialsComponent,
    ContactComponent,
    // ✅ Composants Back
    FooterbackComponent,
    HomebackComponent,
    SidebackComponent,
    HeaderbackComponent,
    ProfilebackComponent,
    LayoutComponent,
    // ✅ Pages et formulaires
    LoginfrontComponent,
    InternshipOffersComponent,
    InternshipOfferFormComponent,
    DashboardOffersComponent,
    DashboardApplicationsComponent,
    ApplicationFormComponent,
    MyApplicationsComponent,
    GlobalStatisticsComponent,
  
    CompanyStatisticsComponent
    
   
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,  // 📌 Gestion des routes
    HttpClientModule,  // 📌 Pour les requêtes API
    ReactiveFormsModule, // 📌 Pour les formulaires dynamiques `[formGroup]`
    FormsModule, // 📌 Pour `ngModel`
    CommonModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    MatInputModule,
    MatFormFieldModule,
    BrowserAnimationsModule,
    MatProgressSpinnerModule,
    NgChartsModule // Ajoute ça dans la liste des imports
    // 📌 Pour éviter les erreurs `ngClass`
  ],
 providers: [StatisticsService],
  bootstrap: [AppComponent]
})
export class AppModule { }
