package com.shootforever.nuclear.event;

/**
 * An abstract class for cancellable events.
 * This class extends the {@link Event} abstract class.
 */
public abstract class CancellableEvent extends Event {
	/**
	 * A flag indicating whether the event has been cancelled.
	 */
	private boolean cancelled;

	/**
	 * Sets the cancellation status of the event.
	 *
	 * @param cancelled {@code true} to cancel the event, {@code false} to allow it.
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * Checks if the event has been cancelled.
	 *
	 * @return {@code true} if the event is cancelled, {@code false} otherwise.
	 */
	public boolean isCancelled() {
		return cancelled;
	}
}
