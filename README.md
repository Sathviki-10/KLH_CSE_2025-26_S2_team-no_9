# Smart Job Portal Search System

## 📌 Project Overview

The **Smart Job Portal Search System** is a web-based platform designed to help applicants efficiently search and find relevant job opportunities from thousands of job postings.

The system allows users to search for jobs based on **skills and location**, making the job-search process faster, easier, and more effective.

## 🎯 Objectives

* Provide efficient job searching.
* Find jobs based on applicant skills.
* Filter jobs according to location.
* Reduce the time required to find suitable jobs.
* Improve the overall job-search experience.

## ✨ Features

* 🔍 Skill-based job search
* 📍 Location-based job filtering
* 💼 Experience-based filtering
* 💰 Salary-based filtering
* 👤 User registration and login
* 🔖 Save jobs
* 📄 View job details
* 🚀 Apply for jobs
* ⭐ Job recommendations

## 🛠️ Tools & Technologies

* **Frontend:** HTML, CSS, JavaScript
* **Backend:** Python, Flask
* **Database:** MySQL
* **Development Environment:** VS Code
* **Version Control:** Git & GitHub

## 🏗️ System Architecture

```text
             USER
               │
               ▼
        ┌───────────────┐
        │   Frontend    │
        │ HTML/CSS/JS   │
        └───────┬───────┘
                │
                ▼
        ┌───────────────┐
        │    Backend    │
        │ Python/Flask  │
        └───────┬───────┘
                │
                ▼
        ┌───────────────┐
        │    MySQL      │
        │   Database    │
        └───────────────┘
```

## 📂 Project Structure

```text
Smart-Job-Portal/
│
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── script.js
│
├── backend/
│   ├── app.py
│   └── requirements.txt
│
├── database/
│   └── database.sql
│
├── screenshots/
│
└── README.md
```

## ⚙️ How to Run

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR-USERNAME/Smart-Job-Portal.git
```

### 2. Open the Project

```bash
cd Smart-Job-Portal
```

### 3. Install Dependencies

```bash
pip install -r requirements.txt
```

### 4. Configure MySQL

Create the required MySQL database and import:

```text
database/database.sql
```

Update the database credentials in the backend configuration.

### 5. Run the Application

```bash
python app.py
```

Open the local URL provided by Flask in your browser.

## 🔎 Example Search

```text
Skills: Java, Python, SQL
Location: Hyderabad
Experience: 0–2 Years
Salary: ₹4–8 LPA
```

The system displays job postings matching the selected criteria.

## 🚀 Future Enhancements

* AI-based job recommendations
* Resume parsing
* Machine-learning-based job matching
* Automated job alerts
* Recruiter dashboard
* Resume upload and analysis
* Real-time job updates

## 👥 Team Members

* Yalamarty Sathviki
* Team Member 2
* Team Member 3
* Team Member 4

## 📄 Project Type

**Academic / Mini Project**

## 📜 License

This project is developed for educational and academic purposes.
