import requests
import json

BASE_URL = "http://localhost:8080"
EMAIL = "leila.updated@example.com"
PASSWORD = "secret456"

def login():
    resp = requests.post(f"{BASE_URL}/auth/login", json={"email": EMAIL, "password": PASSWORD})
    if resp.status_code != 200:
        print(f"Login failed: {resp.status_code} {resp.text}")
        exit(1)
    return resp.json()["token"]

def create_equipement(token, name):
    headers = {"Authorization": f"Bearer {token}"}
    resp = requests.post(f"{BASE_URL}/equipements", json={"nom": name, "disponible": True}, headers=headers)
    if resp.status_code != 201:
        print(f"Create equipement failed: {resp.status_code} {resp.text}")
        exit(1)
    return resp.json()

def assign_equipement(token, intervention_id, equip_id):
    headers = {"Authorization": f"Bearer {token}"}
    resp = requests.post(f"{BASE_URL}/interventions/{intervention_id}/equipements", json={"equipementIds": [equip_id]}, headers=headers)
    print(f"Assign equipement {equip_id} to intervention {intervention_id}: {resp.status_code}")
    if resp.status_code != 200:
        print(resp.text)

def main():
    token = login()
    print("Logged in")

    # Create one equipment
    eq1 = create_equipement(token, "TestEquipC")
    print(f"Created equip 1: {eq1['id']}")

    # Use existing intervention 1
    intervention_id = 1

    # Assign first
    assign_equipement(token, intervention_id, eq1['id'])

    # Assign SAME again
    assign_equipement(token, intervention_id, eq1['id'])

if __name__ == "__main__":
    main()
