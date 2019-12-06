DROP INDEX IF EXISTS custIDandNames CASCADE;
DROP INDEX IF EXISTS hotelIDRoomno CASCADE;
DROP INDEX IF EXISTS BookingDate CASCADE;

CREATE INDEX custIDandNames
ON Customer
(customerID, fName, lName);

CREATE INDEX hotelIDRoomno
ON Booking
(hotelID, roomNo);

CREATE INDEX Bookingdate
ON Booking
(bookingDate);
