-- Insert into Venues
INSERT INTO venue (id, name, contact_number, street, district, state, pincode)
VALUES (1, 'ABC Sports Complex', '9876543210', 'MG Road', 'Bangalore Urban', 'KA', '560001');

INSERT INTO venue (id, name, contact_number, street, district, state, pincode)
VALUES (2, 'XYZ Indoor Stadium', '9876500000', 'Banjara Hills', 'Hyderabad', 'TG', '500001');

INSERT INTO venue (id, name, contact_number, street, district, state, pincode)
VALUES (3, 'Elite Sports Arena', '9123456789', 'Anna Salai', 'Chennai', 'TN', '600001');

INSERT INTO venue (id, name, contact_number, street, district, state, pincode)
VALUES (4, 'Metro Badminton Hub', '9988776655', 'Andheri West', 'Mumbai Suburban', 'MH', '400001');

INSERT INTO venue (id, name, contact_number, street, district, state, pincode)
VALUES (5, 'Grand Indoor Courts', '9871234567', 'Connaught Place', 'New Delhi', 'DL', '110001');


-- Courts for Venue 1: ABC Sports Complex
INSERT INTO badminton_court (id, court_name, venue_id) VALUES (1, 'Court A1', 1);
INSERT INTO badminton_court (id, court_name, venue_id) VALUES (2, 'Court A2', 1);
INSERT INTO badminton_court (id, court_name, venue_id) VALUES (3, 'Court A3', 1);

-- Courts for Venue 2: XYZ Indoor Stadium
INSERT INTO badminton_court (id, court_name, venue_id) VALUES (4, 'Court B1', 2);
INSERT INTO badminton_court (id, court_name, venue_id) VALUES (5, 'Court B2', 2);

-- Courts for Venue 3: Elite Sports Arena
INSERT INTO badminton_court (id, court_name, venue_id) VALUES (6, 'Court C1', 3);
INSERT INTO badminton_court (id, court_name, venue_id) VALUES (7, 'Court C2', 3);
INSERT INTO badminton_court (id, court_name, venue_id) VALUES (8, 'Court C3', 3);

-- Courts for Venue 4: Metro Badminton Hub
INSERT INTO badminton_court (id, court_name, venue_id) VALUES (9, 'Court D1', 4);
INSERT INTO badminton_court (id, court_name, venue_id) VALUES (10, 'Court D2', 4);

-- Courts for Venue 5: Grand Indoor Courts
INSERT INTO badminton_court (id, court_name, venue_id) VALUES (11, 'Court E1', 5);
INSERT INTO badminton_court (id, court_name, venue_id) VALUES (12, 'Court E2', 5);

--users seed data
INSERT INTO users (user_id, username, email, contact_number)
VALUES
  (gen_random_uuid(), 'Amit Sharma', 'amit.sharma@example.com', '9876500010'),
  (gen_random_uuid(), 'Priya Verma', 'priya.verma@example.com', '9876500011'),
  (gen_random_uuid(), 'John Mathew', 'john.mathew@example.com', '9876500012'),
  (gen_random_uuid(), 'Sneha Iyer', 'sneha.iyer@example.com', '9876500013'),
  (gen_random_uuid(), 'David Fernandes', 'david.fernandes@example.com', '9876500014'),
  (gen_random_uuid(), 'Kavya Reddy', 'kavya.reddy@example.com', '9876500015'),
  (gen_random_uuid(), 'Michael Dsouza', 'michael.dsouza@example.com', '9876500016'),
  (gen_random_uuid(), 'Ananya Nair', 'ananya.nair@example.com', '9876500017'),
  (gen_random_uuid(), 'Sarah Thomas', 'sarah.thomas@example.com', '9876500018'),
  (gen_random_uuid(), 'Rahul Khanna', 'rahul.khanna@example.com', '9876500019');


----alter queries
ALTER TABLE users
    ALTER COLUMN user_id SET DATA TYPE UUID
    USING (user_id::uuid);

ALTER TABLE payments
    ALTER COLUMN user_id SET DATA TYPE UUID
    USING (user_id::uuid);

ALTER TABLE booking
    ALTER COLUMN user_id SET DATA TYPE UUID
    USING (user_id::uuid);

--pgcrypto
CREATE EXTENSION IF NOT EXISTS "pgcrypto";


---adding price column to badminton court
ALTER TABLE badminton_court ADD COLUMN price DECIMAL(10,2) NOT NULL DEFAULT 0;

--update query for price
UPDATE public.badminton_court SET price = 400 WHERE id = 1;
UPDATE public.badminton_court SET price = 550 WHERE id = 2;
UPDATE public.badminton_court SET price = 650 WHERE id = 3;
UPDATE public.badminton_court SET price = 350 WHERE id = 4;
UPDATE public.badminton_court SET price = 700 WHERE id = 5;
UPDATE public.badminton_court SET price = 500 WHERE id = 6;
UPDATE public.badminton_court SET price = 450 WHERE id = 7;
UPDATE public.badminton_court SET price = 600 WHERE id = 8;
UPDATE public.badminton_court SET price = 370 WHERE id = 9;
UPDATE public.badminton_court SET price = 680 WHERE id = 10;
UPDATE public.badminton_court SET price = 520 WHERE id = 11;
UPDATE public.badminton_court SET price = 580 WHERE id = 12;

---password column
ALTER TABLE users ADD COLUMN password VARCHAR(255);

UPDATE users SET password = 'kavya.reddy@example.com' WHERE user_id = '440cce6d-d5b0-4f6e-82e3-08f847f7ed8d';
UPDATE users SET password = 'priya.verma@example.com' WHERE user_id = '6450de59-579b-46af-90d5-325ca3927a36';
UPDATE users SET password = 'ananya.nair@example.com' WHERE user_id = '69c682d8-89fa-4d5f-8b05-655f0fe859fc';
UPDATE users SET password = 'john.mathew@example.com' WHERE user_id = '6d96d0e2-4ba9-4d8a-9d3d-58bc9d951b01';
UPDATE users SET password = 'david.fernandes@example.com' WHERE user_id = '81834b8f-6cc5-482a-bb5d-bee9e5c08b06';
UPDATE users SET password = 'amit.sharma@example.com' WHERE user_id = '8ae5e1c1-c0d5-4074-a124-84fda642eba5';
UPDATE users SET password = 'michael.dsouza@example.com' WHERE user_id = 'a9abce7f-963a-4d86-805e-3be5985eca4e';
UPDATE users SET password = 'sarah.thomas@example.com' WHERE user_id = 'd367da15-26cf-4d75-a217-4ddd4e108c5d';
UPDATE users SET password = 'rahul.khanna@example.com' WHERE user_id = 'd733e473-0f89-43a9-9ffa-6aa7adfb7ba9';
UPDATE users SET password = 'sneha.iyer@example.com' WHERE user_id = 'e72c392d-bce6-4cef-aeab-7b2bcf9c5d5c';