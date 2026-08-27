# How to Run - Smart Job Portal Search System (No Database)

## Prerequisites

Make sure these are installed:
- Java 17+ (`java -version`)
- Maven 3.8+ (`mvn -version`)
- Node.js 16+ (`node -v` and `npm -v`)

**No MySQL required!** Data is loaded from corpus files automatically.

---

## Step 1: Start Backend

Open PowerShell and run:

```bash
cd "C:\Users\ADMIN\Desktop\Smart Job Portal Search System\backend"
mvn clean install
mvn spring-boot:run
```

Backend runs at: **http://localhost:8080**

Verify:
```
http://localhost:8080/api/health
```

Expected response:
```json
{
  "status": "ok",
  "message": "Smart Job Portal API is running"
}
```

---

## Step 2: Start Frontend

Open a **new** PowerShell window and run:

```bash
cd "C:\Users\ADMIN\Desktop\Smart Job Portal Search System\frontend"
npm install
npm start
```

Frontend runs at: **http://localhost:3000**

---

## Step 3: Open Application

Browser: **http://localhost:3000**

---

## Step 4: Login

Use any test account:

| Email | Password |
|-------|----------|
| rahul.sharma@example.com | password123 |
| priya.patel@example.com | password123 |
| arjun.reddy@example.com | password123 |
| sneha.iyer@example.com | password123 |
| vikram.singh@example.com | password123 |
| ananya.kapoor@example.com | password123 |
| karthik.raj@example.com | password123 |
| meera.joshi@example.com | password123 |
| rohan.gupta@example.com | password123 |
| divya.nair@example.com | password123 |

Or click **Sign Up** to create a new account.

---

## Quick Test Commands

```bash
# Health check
curl http://localhost:8080/api/health

# Get all jobs
curl http://localhost:8080/api/jobs

# Search jobs
curl "http://localhost:8080/api/jobs/search?q=Python&location=Bangalore"

# Login
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"email\":\"rahul.sharma@example.com\",\"password\":\"password123\"}"
```

---

## Troubleshooting

### Port 8080 in use
Change in `backend\src\main\resources\application.properties`:
```properties
server.port=8081
```

### Port 3000 in use
React will prompt to use another port. Type `Y`.

### Data not loading
Check backend console for errors. Ensure these files exist:
- `backend\src\main\resources\data\skills.txt`
- `backend\src\main\resources\data\users.txt`
- `backend\src\main\resources\data\jobs.txt`
- `backend\src\main\resources\data\saved_jobs.txt`

### CORS errors
Backend has CORS enabled for all origins.

---

## Data Sources

All data is loaded from corpus files at startup:
- `corpus/skills.txt` → skills list
- `corpus/users.txt` → user accounts
- `corpus/jobs.txt` → job postings
- `corpus/locations.txt` → location aliases
- `corpus/search_queries.txt` → search history

No database needed!
