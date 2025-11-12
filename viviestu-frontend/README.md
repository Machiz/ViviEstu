# ViviEstu Frontend

Angular frontend application for the ViviEstu platform - a web application to help university students in Lima find housing.

## Project Overview

ViviEstu is a comprehensive housing platform that connects university students with verified landlords, offering personalized accommodation recommendations based on proximity to universities, budget, public transport, and safety.

## Features

### For Students
- 🔐 User registration and authentication
- 🏫 University verification
- 🔍 Search and filter accommodation listings
- 📍 Location-based search near universities
- ❤️ Favorites management
- 📝 Rental application submission
- 👤 Profile management
- 🆚 Compare multiple listings

### For Landlords
- �� Landlord registration with identity verification
- 🏠 Publish and manage accommodation listings
- 📊 View performance statistics
- 📋 Manage rental applications
- 👤 Profile management

## Technology Stack

- **Framework**: Angular 17
- **Language**: TypeScript
- **Styling**: SCSS
- **HTTP Client**: Angular HttpClient
- **State Management**: RxJS
- **UI Components**: Angular Material
- **Routing**: Angular Router

## Getting Started

### Prerequisites

- Node.js (v18 or higher)
- npm (v9 or higher)
- Angular CLI (v17)

### Installation

1. Navigate to the frontend directory:
```bash
cd viviestu-frontend
```

2. Install dependencies:
```bash
npm install
```

3. Configure environment:
   - The API URL is configured in `src/environments/environment.ts`
   - Default: `https://viviestu-gn1y.onrender.com/api`

### Development Server

Run the development server:
```bash
npm start
```

Or:
```bash
ng serve
```

Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

### Build

Build the project for production:
```bash
npm run build
```

The build artifacts will be stored in the `dist/` directory.

## Project Structure

```
src/
├── app/
│   ├── core/                 # Core module (singleton services, guards, interceptors)
│   │   ├── guards/          # Route guards (auth, role)
│   │   ├── interceptors/    # HTTP interceptors (auth, error)
│   │   ├── models/          # TypeScript interfaces/models
│   │   └── services/        # API services
│   ├── shared/              # Shared module (reusable components)
│   │   ├── header/
│   │   ├── footer/
│   │   └── loading/
│   ├── auth/                # Authentication module
│   │   ├── login/
│   │   ├── register-student/
│   │   └── register-landlord/
│   ├── student/             # Student feature module
│   │   ├── dashboard/
│   │   ├── search-accommodations/
│   │   ├── accommodation-detail/
│   │   ├── favorites/
│   │   ├── applications/
│   │   ├── profile/
│   │   └── compare/
│   └── landlord/            # Landlord feature module
│       ├── dashboard/
│       ├── manage-accommodations/
│       ├── create-accommodation/
│       ├── manage-applications/
│       ├── statistics/
│       └── profile/
├── environments/            # Environment configurations
└── assets/                  # Static assets
```

## Authentication

The application uses JWT-based authentication:

- Tokens are stored in `sessionStorage` (not `localStorage`) for enhanced security
- All authenticated API requests include an `Authorization: Bearer {token}` header
- Automatic logout on 401/403 responses
- Role-based access control (ESTUDIANTE, PROPIETARIO, ADMIN)

## API Integration

The application integrates with the ViviEstu REST API. All services are located in `src/app/core/services/`:

- `auth.service.ts` - Authentication and user management
- `alojamiento.service.ts` - Accommodation listings
- `estudiante.service.ts` - Student profiles
- `propietario.service.ts` - Landlord profiles
- `favoritos.service.ts` - Favorites management
- `solicitud.service.ts` - Rental applications
- `universidad.service.ts` - Universities
- `distrito.service.ts` - Districts
- `comentario.service.ts` - Comments and reviews
- `interaccion.service.ts` - User interactions and analytics

## Routing

The application uses Angular Router with the following main routes:

- `/` - Home page
- `/auth/login` - Login page
- `/auth/register/student` - Student registration
- `/auth/register/landlord` - Landlord registration
- `/student/*` - Student dashboard and features (requires ESTUDIANTE role)
- `/landlord/*` - Landlord dashboard and features (requires PROPIETARIO role)
- `/unauthorized` - Unauthorized access page

## Security Features

1. **Authentication Guards**: Protect routes requiring authentication
2. **Role Guards**: Enforce role-based access control
3. **HTTP Interceptors**: 
   - Automatically attach JWT tokens to requests
   - Handle authentication errors
4. **Session Storage**: Secure token storage
5. **Input Validation**: Frontend validation on all forms

## Design System

The application follows the design system specified in Figma:
- Style Guide: https://www.figma.com/design/h5Lu3wo9zOe3RYRvT7KRNX/ViviEstu
- Prototypes: https://www.figma.com/design/h5Lu3wo9zOe3RYRvT7KRNX/ViviEstu?node-id=399-51

## Testing

Run unit tests:
```bash
npm test
```

Run end-to-end tests:
```bash
npm run e2e
```

## Contributing

1. Create a feature branch
2. Make your changes
3. Run tests and linting
4. Submit a pull request

## License

This project is part of the ViviEstu platform.
