# 🚗 Real-Time Ride Tracking UI (Frontend System Design)

## 🧩 Problem Statement

Design the **frontend architecture** for a ride tracking screen (like Uber) where users can:

* Track driver movement in real-time on a map
* View ride status updates
* See ETA and route information
* Access driver and trip details

---

## ✅ Functional Requirements

### 1. Map & Driver Tracking

* Display map centered on pickup location
* Show:

  * Driver’s live location (updated every 1–3 seconds)
  * Pickup & drop markers
  * Route path

---

### 2. Ride Status

* Show ride states:

  * Driver assigned
  * Arriving
  * Trip started
  * Completed

---

### 3. ETA & Route

* Show:

  * ETA to pickup
  * ETA to destination
* Dynamically update based on driver movement

---

### 4. Driver Info Panel

* Name, photo, rating
* Vehicle details
* Call button

---

### 5. Real-Time Updates

* Driver location updates frequently
* Ride status and ETA update dynamically

---

### 6. User Actions

* Call driver
* Cancel ride
* Expand/collapse ride details

---

## 🔌 API Design

### REST API

```http
GET /ride/:rideId
```

**Response:**

```json
{
  "rideId": "123",
  "status": "ARRIVING",
  "pickup": {...},
  "drop": {...},
  "driver": {
    "id": "d1",
    "name": "...",
    "vehicle": "...",
    "rating": 4.8
  },
  "etaToPickup": 120,
  "etaToDrop": 900
}
```

👉 Single API reduces latency and avoids multiple calls

---

## 🔄 WebSocket Design

### Single WebSocket Connection

### Event Types

```js
{
  type: "DRIVER_LOCATION",
  lat: number,
  lng: number,
  timestamp: number
}
```

```js
{
  type: "RIDE_STATUS",
  status: "ARRIVING" | "STARTED" | "COMPLETED"
}
```

```js
{
  type: "ETA_UPDATE",
  etaToPickup: number,
  etaToDrop: number
}
```

---

## 🧱 Component Architecture

```
<RidePage>
  ├── MapView          // Map + driver marker (imperative updates)
  ├── DriverPanel      // Driver info + call button
  ├── RideInfo         // Pickup/drop + fare
  ├── ETAInfo          // ETA display
</RidePage>
```

---

## 🧠 State Management Strategy

### 1. Server State (API)

* Ride details
* Driver info

👉 Managed via React Query / SWR

---

### 2. Real-Time State (WebSocket)

* Driver location
* Ride status
* ETA

👉 Managed via Zustand / lightweight store

---

### 3. UI State

* Panel expanded/collapsed

👉 Local component state

---

## 🔁 Data Flow

1. Page loads → fetch `/ride/:rideId`
2. Render UI
3. Establish WebSocket connection
4. Receive events → update state/store
5. UI updates selectively

---

## 🎬 Driver Location Handling (Smooth Animation)

### Approach:

* Maintain a **small buffer (queue)** of positions
* Animate sequentially to preserve path
* Drop old points if queue grows too large

```js
queue.push(newLocation)

if (queue.length > 3) {
  queue = queue.slice(-2)
}
```

### Animation:

```js
animateMarker(marker, currentPosition, nextPosition)
```

👉 Uses `requestAnimationFrame` for smooth movement

---

## 🗺️ Map Rendering Strategy

Using Google Maps API:

* Initialize map once
* Add driver marker
* Update marker using:

```js
marker.setPosition({ lat, lng })
```

👉 Avoid React re-renders for map

---

## ⚡ Performance Optimizations

* Avoid React re-renders for high-frequency updates
* Use imperative updates for map
* Memoize UI components (`React.memo`)
* Separate:

  * High-frequency (location)
  * Low-frequency (status, ETA)

---

## 🔄 Handling Burst Updates

* Maintain small buffer instead of dropping all updates
* Process sequentially for smooth path
* Keep only recent points

👉 Balance accuracy vs performance

---

## ❌ Failure Handling

### WebSocket Disconnect:

* Retry with exponential backoff
* Show “Reconnecting…” UI

### Missed Updates:

* Re-fetch `/ride/:rideId` to resync

### GPS Issues:

* Ignore large jumps
* Skip animation for invalid data

---

## ⚙️ Non-Functional Requirements

### 1. Performance

* Smooth animation using `[requestAnimationFrame](https://developer.mozilla.org/en-US/docs/Web/API/Window/requestAnimationFrame)`  
``` js
function animateMarker(marker, start, end, duration = 1000) {
  const startTime = performance.now()

  function animate(currentTime) {
    const elapsed = currentTime - startTime
    const progress = Math.min(elapsed / duration, 1)

    // linear interpolation
    const lat = start.lat + (end.lat - start.lat) * progress
    const lng = start.lng + (end.lng - start.lng) * progress

    marker.setPosition({ lat, lng })

    if (progress < 1) {
      requestAnimationFrame(animate)
    }
  }

  requestAnimationFrame(animate)
}
```
* Avoid full component re-renders

---

### 2. Scalability (Client-side)

* Efficient handling of frequent updates
* Works on low-end devices

---

### 3. Network Resilience

* WebSocket retry + fallback polling
* Graceful degradation

---

### 4. Battery Optimization

* Reduce updates in background tabs
* Pause animations when inactive

---

### 5. Consistency

* Use timestamps to handle out-of-order events

```js
if (incoming.timestamp > current.timestamp) {
  update()
}
```

---

## 🎯 Key Design Principles

* Separate **data ingestion** from **rendering**
* Use **imperative updates** for high-frequency UI
* Maintain **single source of truth**
* Balance **accuracy vs performance**
* Design for **real-world constraints (network, device, scale)**

---
