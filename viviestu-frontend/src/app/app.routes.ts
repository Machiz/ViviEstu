import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

import { HomeComponent } from './home/home.component';
import { UnauthorizedComponent } from './unauthorized/unauthorized.component';

// Auth components
import { LoginComponent } from './auth/login/login.component';
import { RegisterStudentComponent } from './auth/register-student/register-student.component';
import { RegisterLandlordComponent } from './auth/register-landlord/register-landlord.component';

// Student components
import { DashboardComponent as StudentDashboardComponent } from './student/dashboard/dashboard.component';
import { SearchAccommodationsComponent } from './student/search-accommodations/search-accommodations.component';
import { AccommodationDetailComponent } from './student/accommodation-detail/accommodation-detail.component';
import { FavoritesComponent } from './student/favorites/favorites.component';
import { ApplicationsComponent as StudentApplicationsComponent } from './student/applications/applications.component';
import { ProfileComponent as StudentProfileComponent } from './student/profile/profile.component';
import { CompareComponent } from './student/compare/compare.component';

// Landlord components
import { DashboardComponent as LandlordDashboardComponent } from './landlord/dashboard/dashboard.component';
import { ManageAccommodationsComponent } from './landlord/manage-accommodations/manage-accommodations.component';
import { CreateAccommodationComponent } from './landlord/create-accommodation/create-accommodation.component';
import { ManageApplicationsComponent } from './landlord/manage-applications/manage-applications.component';
import { StatisticsComponent } from './landlord/statistics/statistics.component';
import { ProfileComponent as LandlordProfileComponent } from './landlord/profile/profile.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'unauthorized', component: UnauthorizedComponent },
  
  // Auth routes
  {
    path: 'auth',
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'register/student', component: RegisterStudentComponent },
      { path: 'register/landlord', component: RegisterLandlordComponent }
    ]
  },

  // Student routes
  {
    path: 'student',
    canActivate: [authGuard, roleGuard(['ESTUDIANTE', 'ADMIN'])],
    children: [
      { path: 'dashboard', component: StudentDashboardComponent },
      { path: 'search', component: SearchAccommodationsComponent },
      { path: 'accommodation/:id', component: AccommodationDetailComponent },
      { path: 'favorites', component: FavoritesComponent },
      { path: 'applications', component: StudentApplicationsComponent },
      { path: 'profile', component: StudentProfileComponent },
      { path: 'compare', component: CompareComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

  // Landlord routes
  {
    path: 'landlord',
    canActivate: [authGuard, roleGuard(['PROPIETARIO', 'ADMIN'])],
    children: [
      { path: 'dashboard', component: LandlordDashboardComponent },
      { path: 'accommodations', component: ManageAccommodationsComponent },
      { path: 'accommodations/create', component: CreateAccommodationComponent },
      { path: 'accommodations/edit/:id', component: CreateAccommodationComponent },
      { path: 'applications', component: ManageApplicationsComponent },
      { path: 'statistics', component: StatisticsComponent },
      { path: 'profile', component: LandlordProfileComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

  { path: '**', redirectTo: '' }
];
