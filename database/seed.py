import json
import mysql.connector
import os
import sys

sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from backend.config import Config

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
CORPUS_DIR = os.path.join(BASE_DIR, "corpus")

def get_connection():
    return mysql.connector.connect(
        host=Config.MYSQL_HOST,
        user=Config.MYSQL_USER,
        password=Config.MYSQL_PASSWORD,
        database=Config.MYSQL_DB
    )

def load_json(filename):
    path = os.path.join(CORPUS_DIR, filename)
    with open(path, "r", encoding="utf-8") as f:
        return json.load(f)

def seed():
    conn = get_connection()
    cursor = conn.cursor()

    skills = load_json("skills.txt")
    for skill in skills:
        cursor.execute("INSERT IGNORE INTO skills (skill_name) VALUES (%s)", (skill,))
    print(f"Seeded {len(skills)} skills.")

    users = load_json("users.txt")
    for user in users:
        cursor.execute("INSERT IGNORE INTO users (id, name, email, password, location, experience) VALUES (%s, %s, %s, %s, %s, %s)",
                       (user.get('id'), user.get('name'), user.get('email'), user.get('password'), user.get('location'), user.get('experience')))
    print(f"Seeded {len(users)} users.")

    jobs = load_json("jobs.txt")
    for job in jobs:
        cursor.execute("""
            INSERT INTO jobs (id, title, company, location, description, experience, salary, job_type)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
            ON DUPLICATE KEY UPDATE title=VALUES(title)
        """, (job.get('id'), job.get('title'), job.get('company'), job.get('location'),
              job.get('description'), job.get('experience'), job.get('salary'), job.get('job_type')))
        for skill in job.get('skills', []):
            cursor.execute("SELECT id FROM skills WHERE skill_name = %s", (skill,))
            row = cursor.fetchone()
            if row:
                cursor.execute("INSERT IGNORE INTO job_skills (job_id, skill_id) VALUES (%s, %s)", (job.get('id'), row[0]))
    print(f"Seeded {len(jobs)} jobs with skills.")

    conn.commit()
    cursor.close()
    conn.close()
    print("Database seeding completed successfully.")

if __name__ == '__main__':
    seed()
