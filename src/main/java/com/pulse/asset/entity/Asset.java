package com.pulse.asset.entity;

import java.time.Instant;

import org.aspectj.weaver.ast.Instanceof;
import org.springframework.security.config.annotation.AlreadyBuiltException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "asset")
public class Asset {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tenant_id", nullable = false)
	private String tenantId;

	@Column(name = "external_id", nullable = false)
	private String externalId;

	private boolean hidden;
	private String name;

	@Enumerated(EnumType.STRING)
	private AssetType type;

	@Enumerated(EnumType.STRING)
	private AssetStatus status;

	@Column(name = "tags_csv")
	private String tagsCsv;

	@Version
	private Long version;

	@Column(name = "created_at", updatable = false)
	private Instant createdAt;

	@Column(name = "updated_at")
	private Instant updatedAt;

	protected Asset() {

	}

	private Asset(Builder builder) {

		this.tagsCsv = builder.tagsCsv;
		this.tenantId = builder.tenantId;
		this.externalId = builder.externalId;
		this.name = builder.name;
		this.hidden = builder.hidden;
		this.status = builder.status;
		this.type = builder.type;

	}

	public static class Builder {
		private String tenantId;
		private String externalId;
		private String name;
		private AssetType type;
		private AssetStatus status;
		private String tagsCsv;
		private boolean hidden;

		public Builder tenantId(String tenantId) {
			this.tenantId = tenantId;
			return this;
		}

		public Builder externalId(String externalId) {
			this.externalId = externalId;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder AssteType(AssetType type) {
			this.type = type;
			return this;
		};

		public Builder hidden(boolean hidden) {
			this.hidden = hidden;
			return this;
		}

		public Builder AssteStatus(AssetStatus status) {
			this.status = status;
			return this;
		}

		public Builder tagsCsv(String tagsCsv) {
			this.tagsCsv = tagsCsv;
			return this;
		}

		public Asset build() {
			return new Asset(this);
		}
	}

	@PrePersist
	private void OnSave() {
		this.createdAt = Instant.now();
		this.updatedAt = Instant.now();
	}

	@PreUpdate
	private void OnUpdate() {
		this.updatedAt = Instant.now();
	}

	public long getId() {
		return id;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getExternalId() {
		return externalId;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AssetType getType() {
		return type;
	}

	public AssetStatus getStatus() {
		return status;
	}

	public void setStatus(AssetStatus status) {
		this.status = status;
	}

	public String getTagsCsv() {
		return tagsCsv;
	}

	public void setTagsCsv(String tagsCsv) {
		this.tagsCsv = tagsCsv;
	}

	public Long getVersion() {
		return version;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;
		if (!(o instanceof Asset))
			return false;

		Asset x = (Asset) o;
		return (x.id != null) && this.id == x.id;

	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "Asset{name=%s, type=%s,  status=%s}".formatted(this.name, this.type, this.status);
	}

}
