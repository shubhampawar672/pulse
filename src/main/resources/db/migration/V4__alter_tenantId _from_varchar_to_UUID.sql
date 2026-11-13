ALTER TABLE asset
    ADD CONSTRAINT fk_asset_tenant
    FOREIGN KEY (tenant_id) REFERENCES tenant (id);