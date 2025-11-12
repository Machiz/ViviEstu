# ViviEstu Frontend - Deployment Guide

## Overview

This guide explains how to deploy the ViviEstu Angular frontend application to production.

## Prerequisites

- Node.js 18+ and npm 9+
- Access to a static hosting provider (Netlify, Vercel, AWS S3, etc.)
- Backend API running at `https://viviestu-gn1y.onrender.com/api`

## Local Development

### 1. Install Dependencies

```bash
cd viviestu-frontend
npm install
```

### 2. Run Development Server

```bash
npm start
# or
ng serve
```

Navigate to `http://localhost:4200/`

### 3. Build for Production

```bash
npm run build
```

The build artifacts will be in `viviestu-frontend/dist/viviestu-frontend/`

## Deployment Options

### Option 1: Netlify

1. **Install Netlify CLI** (optional):
```bash
npm install -g netlify-cli
```

2. **Deploy**:
```bash
cd viviestu-frontend
npm run build
netlify deploy --prod --dir=dist/viviestu-frontend
```

Or use Netlify's web interface:
- Connect your GitHub repository
- Build command: `cd viviestu-frontend && npm install && npm run build`
- Publish directory: `viviestu-frontend/dist/viviestu-frontend`

### Option 2: Vercel

1. **Install Vercel CLI** (optional):
```bash
npm install -g vercel
```

2. **Deploy**:
```bash
cd viviestu-frontend
vercel --prod
```

Or use Vercel's web interface:
- Connect your GitHub repository
- Framework preset: Angular
- Root directory: `viviestu-frontend`
- Build command: `npm run build`
- Output directory: `dist/viviestu-frontend`

### Option 3: AWS S3 + CloudFront

1. **Build the application**:
```bash
cd viviestu-frontend
npm run build
```

2. **Create S3 bucket**:
```bash
aws s3 mb s3://viviestu-frontend
```

3. **Upload files**:
```bash
aws s3 sync dist/viviestu-frontend/ s3://viviestu-frontend --delete
```

4. **Configure S3 for static website hosting**:
- Enable static website hosting
- Set index document: `index.html`
- Set error document: `index.html` (for Angular routing)

5. **Set up CloudFront distribution**:
- Origin: S3 bucket
- Viewer Protocol Policy: Redirect HTTP to HTTPS
- Error Pages: Configure 404 to return `index.html` with 200 status

### Option 4: GitHub Pages

1. **Install angular-cli-ghpages**:
```bash
npm install -g angular-cli-ghpages
```

2. **Build and deploy**:
```bash
cd viviestu-frontend
ng build --base-href /ViviEstu/
npx angular-cli-ghpages --dir=dist/viviestu-frontend
```

## Environment Configuration

The API URL is configured in:
- `src/environments/environment.ts` (development)
- `src/environments/environment.development.ts` (development)

For production, update the API URL if needed before building:

```typescript
export const environment = {
  production: true,
  apiUrl: 'https://viviestu-gn1y.onrender.com/api'
};
```

## Build Optimization

### Enable Production Mode

Production builds are automatically optimized with:
- Ahead-of-Time (AOT) compilation
- Tree shaking
- Minification
- Uglification
- Dead code elimination

### Analyze Bundle Size

```bash
npm run build -- --stats-json
npx webpack-bundle-analyzer dist/viviestu-frontend/stats.json
```

## Post-Deployment Checklist

- [ ] Verify all pages load correctly
- [ ] Test authentication flow
- [ ] Test student registration
- [ ] Test landlord registration
- [ ] Test accommodation search
- [ ] Test creating accommodation (landlord)
- [ ] Test favorites functionality
- [ ] Test application submission
- [ ] Verify API integration
- [ ] Test on mobile devices
- [ ] Verify HTTPS is working
- [ ] Check browser console for errors

## Troubleshooting

### 404 Errors on Page Refresh

If you get 404 errors when refreshing pages (common with Angular routing):

**Netlify**: Create `_redirects` file in `src/` folder:
```
/*    /index.html   200
```

**Vercel**: Create `vercel.json`:
```json
{
  "rewrites": [
    { "source": "/(.*)", "destination": "/index.html" }
  ]
}
```

**Apache**: Add `.htaccess`:
```apache
<IfModule mod_rewrite.c>
  RewriteEngine On
  RewriteBase /
  RewriteRule ^index\.html$ - [L]
  RewriteCond %{REQUEST_FILENAME} !-f
  RewriteCond %{REQUEST_FILENAME} !-d
  RewriteRule . /index.html [L]
</IfModule>
```

### CORS Issues

If you encounter CORS errors:
- Ensure the backend API has CORS enabled
- Check that the API URL in environment files is correct
- Verify the backend accepts requests from your frontend domain

### Large Bundle Size

If bundle size is too large:
- Enable lazy loading for feature modules
- Use OnPush change detection strategy
- Remove unused dependencies
- Consider code splitting

## Performance Optimization

1. **Enable Compression**: Configure your server to gzip/brotli compress assets
2. **CDN**: Use a CDN for faster asset delivery
3. **Caching**: Set appropriate cache headers for static assets
4. **Lazy Loading**: Implement lazy loading for routes (future enhancement)

## Monitoring

Consider adding:
- Google Analytics
- Sentry for error tracking
- LogRocket for session replay
- New Relic for performance monitoring

## Security Notes

- JWT tokens are stored in sessionStorage (cleared on tab close)
- All API requests use HTTPS
- CORS is configured on the backend
- XSS protection through Angular's built-in sanitization
- CSRF protection via JWT authentication

## Support

For issues or questions:
- Check the README.md in viviestu-frontend/
- Review the backend API documentation
- Contact the development team
