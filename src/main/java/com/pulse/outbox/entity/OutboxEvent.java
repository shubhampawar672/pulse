package com.pulse.outbox.entity;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.pulse.common.tenant.Tenant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "event_id")
	private UUID eventId;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private OutboxStatus status = OutboxStatus.NEW;

	@ManyToOne
	@JoinColumn(name = "tenant_id", referencedColumnName = "id", nullable = false)
	private Tenant tenantId;

	@Column(name = "payload")
	private String payload;

	@Column(name = "attempts")
	private int attempts;

	@Column(name = "last_error")
	private String lastError;

	@Column(name = "created_at")
	private Instant createdAt;

	@Column(name = "sent_at")
	private Instant sentAt;

	@Column(name = "next_attempt_at")
	private Instant nextAttemptAt;

	@Column(name = "event_type", nullable = false, updatable = false, length = 150)
	@Enumerated(EnumType.STRING)
	private EventType eventType;

	@Version
	@Column(name = "version")
	private Instant version;

	protected OutboxEvent() {
		// Required by JPA
	}

	public OutboxEvent(Tenant tenant, EventType eventType, String payload) {

		this.eventId = UUID.randomUUID();
		this.tenantId = Objects.requireNonNull(tenant, "tenant is required");
		this.eventType = Objects.requireNonNull(eventType, "eventType is required");
		this.payload = Objects.requireNonNull(payload, "payload is required");
	}

	public EventType getEventType() {
		return eventType;
	}

	@PrePersist
	private void onCreate() {
		Instant now = Instant.now();

		if (eventId == null) {
			eventId = UUID.randomUUID();
		}

		createdAt = now;

		if (nextAttemptAt == null) {
			nextAttemptAt = now;
		}
	}

	public void markSent() {
		attempts++;
		status = OutboxStatus.SENT;
		sentAt = Instant.now();
		lastError = null;
	}

	public void markFailed(String error, Instant retryAt) {
		attempts++;
		status = OutboxStatus.FAILED;
		lastError = error;
		nextAttemptAt = Objects.requireNonNull(retryAt, "retryAt is required");
	}

	public UUID getEventId() {
		return eventId;
	}

	public OutboxStatus getStatus() {
		return status;
	}

	public Tenant getTenantId() {
		return tenantId;
	}

	public String getPayload() {
		return payload;
	}

	public int getAttempts() {
		return attempts;
	}

	public Long getId() {
		return id;
	}

	public String getLastError() {
		return lastError;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getSentAt() {
		return sentAt;
	}

	public Instant getNextAttemptAt() {
		return nextAttemptAt;
	}

	public Instant getVersion() {
		return version;
	}

}
