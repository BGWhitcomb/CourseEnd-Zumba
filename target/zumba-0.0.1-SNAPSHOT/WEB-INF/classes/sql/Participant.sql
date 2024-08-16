CREATE TABLE Participant (
    pid INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL,
    bid INT,
    FOREIGN KEY (bid) REFERENCES Batch(bid)
);

