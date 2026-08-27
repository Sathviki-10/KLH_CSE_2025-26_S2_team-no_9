# Smart Job Portal Search System

## Project Title
Smart Job Portal Search System

## Abstract
The Smart Job Portal Search System is a modern, responsive web application designed to help job seekers find relevant job opportunities efficiently using intelligent search and filtering mechanisms. Unlike traditional job portals that rely on simple keyword matching, this system implements relevance-based ranking to prioritize jobs that best match a user's skills, location preferences, experience level, and job type requirements.

## Problem Statement
Traditional job portals suffer from information overload, returning hundreds of irrelevant results for simple queries. Job seekers spend excessive time manually filtering through listings that don't match their qualifications or preferences. The lack of intelligent ranking and personalized recommendations makes the job search process inefficient and frustrating.

## Objectives
1. Implement an efficient search engine with relevance ranking (0-100 score) based on multiple criteria.
2. Provide advanced filtering capabilities (skills, location, experience, salary, job type).
3. Develop a modern, responsive, professional UI with a blue technology theme.
4. Enable personalized job recommendations based on user profiles.
5. Support job saving functionality and user profile management.
6. Create a scalable backend using Java Spring Boot with REST APIs.
7. Use MySQL for reliable data storage and retrieval.

## Features

### Smart Search & Ranking
- Keyword search across job title, company, and description.
- Skill-based matching with relevance scoring.
- Location matching with alias support (e.g., Bangalore → Bengaluru).
- Experience level matching.
- Job type filtering.
- Salary range filtering.

### User Features
- User registration and profile management.
- Save favorite jobs.
- View personalized job recommendations.
- Browse saved jobs.
- Responsive profile dashboard.

### Advanced Filtering
- Filter by skills (multiple skills supported).
- Filter by location (dropdown with popular cities).
- Filter by experience level.
- Filter by job type (Full-time, Part-time, Contract, Internship, Remote).
- Filter by salary range.
- Filter by company name.
- Clear all filters button.

### UI/UX Features
- Professional blue technology theme.
- Modern dashboard-style interface.
- Responsive design for mobile, tablet, and desktop.
- Clean typography with Inter font family.
- Rounded cards with subtle shadows.
- Blue gradients and white content areas.
- Professional icons (SVG).
- Smooth hover effects and transitions.
- Loading states and empty states.
- Mobile-responsive navigation with hamburger menu.

## Technologies

### Frontend
- React 18
- JavaScript (ES6+)
- HTML5
- CSS3 (Modern features: Grid, Flexbox, Custom Properties)
- React Router DOM v6
- Axios for HTTP requests

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Web
- Maven for dependency management
- Lombok for boilerplate reduction

### Database
- MySQL 8.0
- JDBC connector

### Development Tools
- VS Code / IntelliJ IDEA
- Git & GitHub
- Postman (for API testing)

## System Architecture

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│                 │     │                 │     │                 │
│  React Client   │────▶│  Spring Boot    │────▶│   MySQL DB      │
│  (Frontend)     │     │  REST API       │     │                 │
│                 │◀────│  (Backend)      │◀────│                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

### Architecture Layers

1. **Presentation Layer (React)**
   - Components: Header, SearchBar, JobCard, JobDetails
   - Pages: Home, JobResults, Profile, Login
   - Services: API service for HTTP requests
   - State: React hooks for local state management

2. **API Layer (Spring Boot Controllers)**
   - `JobController`: Job search, filtering, details
   - `UserController`: User management
   - `RecommendationController`: Job recommendations

3. **Service Layer**
   - `JobService`: Business logic for jobs
   - `UserService`: Business logic for users
   - `SearchService`: Search and filtering logic

4. **Data Access Layer (Spring Data JPA)**
   - `JobRepository`: Job data access
   - `SkillRepository`: Skill data access
   - `JobSkillRepository`: Job-skill mapping
   - `SavedJobRepository`: Saved jobs access
   - `UserRepository`: User data access

5. **Database Layer (MySQL)**
   - Normalized schema with foreign keys
   - Indexed columns for performance

## Installation Steps

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- MySQL 8.0 or higher
- Maven 3.8 or higher
- Git

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Smart-Job-Portal-Search-System
```

### 2. Database Setup

#### Create MySQL Database
```sql
CREATE DATABASE IF NOT EXISTS smart_job_portal;
```

#### Run Schema Script
```bash
mysql -u root -p smart_job_portal < database/schema.sql
```

#### Seed the Database
```bash
python database/seed.py
```

Or use the provided `data.sql` if using Spring Boot's auto-initialization.

### 3. Backend Setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend will start at `http://localhost:8080`.

#### Configure Database Credentials
Edit `backend/src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

### 4. Frontend Setup

```bash
cd frontend
npm install
npm start
```

The frontend will start at `http://localhost:3000`.

### 5. Test the Application
1. Open browser and navigate to `http://localhost:3000`
2. Search for jobs using keywords like "Python", "React", "Java"
3. Try advanced filters
4. Create a user account and test saved jobs

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Endpoints

#### Jobs
- `GET /jobs` - Get all jobs with pagination
  - Query params: `page`, `limit`
- `GET /jobs/search` - Search jobs
  - Query params: `q`, `skills`, `location`, `experience`, `job_type`, `salary`, `company`, `sort`, `page`, `limit`
- `GET /jobs/{id}` - Get job by ID
- `GET /jobs/{id}/skills` - Get skills for a job
- `GET /jobs/{id}/similar` - Get similar jobs
- `POST /jobs/{userId}/save` - Save a job
  - Query params: `jobId`
- `DELETE /jobs/{userId}/unsave/{jobId}` - Unsave a job
- `GET /jobs/user/{userId}/saved` - Get saved jobs for user

#### Users
- `POST /users` - Create a new user
  - Body: `name`, `email`, `password`, `location`, `experience`
- `GET /users/{id}` - Get user by ID
- `GET /users` - Get all users

#### Recommendations
- `GET /recommendations?user_id={id}` - Get job recommendations for user

### Response Format
All responses follow this structure:
```json
{
  "success": true,
  "message": "Success",
  "data": {}
}
```

## Database Structure

### Tables

#### `users`
| Column | Type | Description |
|--------|------|-------------|
| id | INT (PK) | Unique user identifier |
| name | VARCHAR(255) | User's full name |
| email | VARCHAR(255) | Unique email address |
| password | VARCHAR(255) | User password |
| location | VARCHAR(255) | User's location |
| experience | VARCHAR(100) | User's experience level |
| created_at | TIMESTAMP | Account creation timestamp |

#### `skills`
| Column | Type | Description |
|--------|------|-------------|
| id | INT (PK) | Unique skill identifier |
| skill_name | VARCHAR(100) | Name of the skill |

#### `jobs`
| Column | Type | Description |
|--------|------|-------------|
| id | INT (PK) | Unique job identifier |
| title | VARCHAR(255) | Job title |
| company | VARCHAR(255) | Company name |
| location | VARCHAR(255) | Job location |
| description | TEXT | Job description |
| experience | VARCHAR(100) | Required experience |
| salary | VARCHAR(100) | Salary range |
| job_type | VARCHAR(100) | Type of job |
| created_at | TIMESTAMP | Post creation timestamp |

#### `job_skills`
| Column | Type | Description |
|--------|------|-------------|
| job_id | INT (FK) | Reference to jobs table |
| skill_id | INT (FK) | Reference to skills table |

#### `saved_jobs`
| Column | Type | Description |
|--------|------|-------------|
| id | INT (PK) | Unique identifier |
| user_id | INT (FK) | Reference to users table |
| job_id | INT (FK) | Reference to jobs table |
| created_at | TIMESTAMP | Save timestamp |

## Search Ranking Methodology

The search system uses a simplified relevance scoring approach:

1. **Keyword Matching**: Matches query terms against job title, company, and description.
2. **Skill Matching**: Checks if user-specified skills match job requirements.
3. **Location Matching**: Supports location aliases (e.g., Bangalore = Bengaluru).
4. **Experience Matching**: Filters jobs based on required experience level.
5. **Job Type Matching**: Matches preferred job type.
6. **Salary Matching**: Filters based on salary range.

### Filtering Logic
- All filter criteria are combined using AND logic.
- Empty filter values are ignored (treated as "match all").
- Results can be sorted by relevance, salary, or experience.
- Pagination is supported with configurable page sizes.

### Location Aliases
The system supports common location aliases:
- Bangalore ↔ Bengaluru
- Mumbai ↔ Bombay
- Delhi ↔ New Delhi
- Hyderabad ↔ Secunderabad
- Chennai ↔ Madras
- Gurgaon ↔ Gurugram
- Mysore ↔ Mysuru
- And more...

## Testing the Search Functionality

### Test Cases

1. **Keyword Search**
   ```bash
   curl "http://localhost:8080/api/jobs/search?q=Python"
   ```
   Expected: Returns jobs containing "Python" in title, company, or description.

2. **Location Filter**
   ```bash
   curl "http://localhost:8080/api/jobs/search?location=Bangalore"
   ```
   Expected: Returns jobs in Bangalore.

3. **Multiple Filters**
   ```bash
   curl "http://localhost:8080/api/jobs/search?q=developer&location=Hyderabad&job_type=Full-time"
   ```
   Expected: Returns developer jobs in Hyderabad that are full-time.

4. **Location Alias**
   ```bash
   curl "http://localhost:8080/api/jobs/search?location=Bengaluru"
   ```
   Expected: Returns jobs in Bangalore (alias matched).

5. **Empty Search**
   ```bash
   curl "http://localhost:8080/api/jobs"
   ```
   Expected: Returns all jobs with pagination.

6. **Job Details**
   ```bash
   curl "http://localhost:8080/api/jobs/1"
   ```
   Expected: Returns job with ID 1 including skills.

### Using Postman
Import the provided `postman_collection.json` (if available) to test all endpoints.

## Future Enhancements

1. **Email Notifications**: Send email alerts when new matching jobs are posted.
2. **Resume Parsing**: Auto-extract skills and experience from uploaded resumes.
3. **Company Reviews**: Allow users to rate and review companies.
4. **Interview Scheduling**: Integrate calendar and interview booking.
5. **Advanced Analytics**: Provide recruiters with insights on job posting performance.
6. **Mobile Application**: React Native app for iOS and Android.
7. **AI-Powered Recommendations**: Implement machine learning for better job matching.
8. **Chatbot Support**: AI chatbot for career guidance.
9. **Salary Insights**: Show salary trends and market rates.
10. **Skill Assessments**: Allow users to take skill tests.

## Team Members

| Name | Role |
|------|------|
| Team Smart Job Portal | Full Stack Development |

## License
This project is created for educational purposes.

## Contact
For questions or support, please open an issue in the GitHub repository.
