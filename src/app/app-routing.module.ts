import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ContactComponent } from './components/contact/contact.component';
import { HomebackComponent } from './components/homeback/homeback.component';
import { HeaderbackComponent } from './components/headerback/headerback.component';
import { LoginfrontComponent } from './loginfront/loginfront.component';
import { ProfilebackComponent } from './profileback/profileback.component';

import { InternshipOfferFormComponent } from './components/internship-offer-form/internship-offer-form.component';
import { DashboardOffersComponent } from './components/dashboard-offers/dashboard-offers.component';
import { InternshipOffersComponent } from './intershipoffers/internship-offers.component';
import { DashboardApplicationsComponent } from './components/dashboard-applications/dashboard-applications.component';
import { ApplicationFormComponent } from './components/applications-form/applications-form.component';
import { MyApplicationsComponent } from './components/my-applications/my-applications.component';
import { GlobalStatisticsComponent } from './statistics/global-statistics/global-statistics.component';
import { CompanyStatisticsComponent } from './statistics/company-statistics/company-statistics.component';

const routes: Routes = [
  // 🎯 Route par défaut → Redirige vers la page d'accueil du front
  { path: '', redirectTo: 'front', pathMatch: 'full' },

  // 🎯 Routes du FRONT
  { path: 'front', component: HomeComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'login', component: LoginfrontComponent },
  { path: 'my-applications', component: MyApplicationsComponent },

  // 🎯 Liste des offres de stage (Front)
  { path: 'internship-list', component: InternshipOffersComponent },

  // 🎯 Routes du BACK-OFFICE
  { 
    path: 'backoffice', component: ProfilebackComponent, 
    children: [
      { path: 'internship-offer-form', component: InternshipOfferFormComponent },
      { path: 'application-form/:offerId', component: ApplicationFormComponent },
      { path: 'dashboard-apps', component: DashboardApplicationsComponent },
      { path: 'dashboard-offers', component: DashboardOffersComponent },
      { path: 'global-statistics', component: GlobalStatisticsComponent },
  { path: 'company-statistics', component: CompanyStatisticsComponent }
    ] 
  },

  // 🎯 Gestion des erreurs (redirection vers la page d'accueil si URL inconnue)
  { path: '**', redirectTo: 'front', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: false })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
