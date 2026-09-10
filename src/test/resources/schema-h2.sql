CREATE TABLE IF NOT EXISTS fuel_transactions (
    id UUID PRIMARY KEY,
    external_transaction_id VARCHAR(128) NOT NULL UNIQUE,
    driver_id VARCHAR(64) NOT NULL,
    vehicle_id VARCHAR(64) NOT NULL,
    card_number_masked VARCHAR(32) NOT NULL,
    merchant_name VARCHAR(255) NOT NULL,
    merchant_location VARCHAR(255) NOT NULL,
    gallons DECIMAL(12, 3) NOT NULL,
    price_per_gallon DECIMAL(12, 4) NOT NULL,
    total_amount DECIMAL(14, 2) NOT NULL,
    transaction_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    processed_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_fuel_tx_driver_timestamp
    ON fuel_transactions (driver_id, transaction_timestamp);
