CREATE TABLE asset (
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tenant_id    VARCHAR(64)  NOT NULL,
    external_id  VARCHAR(255) NOT NULL,
    type         VARCHAR(32)  NOT NULL,
    name         VARCHAR(255) NOT NULL,
    status       VARCHAR(32)  NOT NULL,
    tags_csv     VARCHAR(512),
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT now(),
    version      BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT uq_asset_tenant_external UNIQUE (tenant_id, external_id)
);

CREATE INDEX idx_asset_tenant_status_type ON asset (tenant_id, status, type);