CREATE TABLE app.user_sessions (
                                   id                   UUID PRIMARY KEY,
                                   username             VARCHAR(255) NOT NULL,
                                   device_id            VARCHAR(255),
                                   access_token         TEXT         NOT NULL,
                                   refresh_token        TEXT         NOT NULL UNIQUE,
                                   access_token_expiry  TIMESTAMP WITH TIME ZONE NOT NULL,
                                   refresh_token_expiry TIMESTAMP WITH TIME ZONE NOT NULL,
                                   status               VARCHAR(20)  NOT NULL,
                                   CONSTRAINT chk_user_sessions_status CHECK (status IN ('ACTIVE','USED','REVOKED'))
);

CREATE INDEX idx_user_sessions_username      ON app.user_sessions(username);
CREATE INDEX idx_user_sessions_refresh_token ON app.user_sessions(refresh_token);