DELETE FROM asset;
ALTER TABLE asset ALTER COLUMN tenant_id TYPE UUID USING tenant_id::uuid;

CREATE TABLE tenant (
    id          UUID PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    slug        VARCHAR(100) NOT NULL,
    status      VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    version     BIGINT NOT NULL DEFAULT 0,
    created_at  TIMESTAMP(6) WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP(6) WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_tenant_slug UNIQUE (slug),
    CONSTRAINT ck_tenant_status CHECK (status IN ('ACTIVE', 'SUSPENDED', 'CLOSED'))
);

CREATE TABLE outbox_event (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id        UUID NOT NULL,
    event_type      VARCHAR(150) NOT NULL,
    tenant_id       UUID NOT NULL,            -- changed from VARCHAR(36)
    payload         TEXT NOT NULL,
    status          VARCHAR(20) NOT NULL DEFAULT 'NEW',
    attempts        INTEGER NOT NULL DEFAULT 0,
    next_attempt_at TIMESTAMP(6) WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_error      TEXT,
    created_at      TIMESTAMP(6) WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at         TIMESTAMP(6) WITH TIME ZONE,
    version         TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    CONSTRAINT uk_outbox_event_event_id UNIQUE (event_id),
    CONSTRAINT fk_outbox_event_tenant FOREIGN KEY (tenant_id) REFERENCES tenant (id),
    CONSTRAINT ck_outbox_event_status CHECK (status IN ('NEW', 'SENT', 'FAILED')),
    CONSTRAINT ck_outbox_event_attempts CHECK (attempts >= 0)
);

CREATE INDEX idx_outbox_event_status_next_attempt ON outbox_event (status, next_attempt_at);
CREATE INDEX idx_outbox_event_tenant ON outbox_event (tenant_id);