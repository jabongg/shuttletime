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
