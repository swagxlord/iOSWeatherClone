# Android Weather App Clone Tasks

This tracker is derived from [docs/prd.md](prd.md). Keep the PRD as the source of truth for product requirements, and use this file to choose the next implementation slice.

## Current Focus

Phase 1 starts with a fake-data vertical slice:

- [ ] Create project/module structure for core domain and weather feature work.
- [ ] Add Navigation 3 app route keys and navigation host foundation.
- [ ] Add app-owned weather domain models.
- [ ] Add a fake weather repository.
- [ ] Add MVI contracts for weather screen state, intents, and effects.
- [ ] Build the first Weather Detail screen in Jetpack Compose.
- [ ] Render current conditions, hourly forecast, 10-day forecast, and weather detail cards.
- [ ] Keep provider DTOs out of UI-facing models.

## Phase 1: Core Forecast App

- [ ] Project/module setup.
- [ ] Navigation 3 setup with app-owned route keys and back stack.
- [ ] Open-Meteo Forecast API integration.
- [ ] WeatherRepository interface and implementation.
- [ ] Current weather header.
- [ ] Hourly forecast carousel.
- [ ] 10-day forecast card.
- [ ] Basic detail cards.
- [ ] Location search.
- [ ] Saved locations.
- [ ] Room/DataStore persistence.
- [ ] Pull to refresh.
- [ ] Loading/error states.
- [ ] Local weather cache.

## Phase 2: Visual Polish

- [ ] Weather-based backgrounds.
- [ ] Condition icon mapping.
- [ ] Temperature range bars.
- [ ] Swipe between saved locations.
- [ ] Better empty/error states.
- [ ] Unit settings.
- [ ] Smooth card animations.

## Phase 3: Expanded Weather Data

- [ ] Open-Meteo Air Quality API.
- [ ] AQI card.
- [ ] AQI detail screen.
- [ ] Moon phase calculation.
- [ ] Better precipitation timeline.
- [ ] NWS severe alerts.
- [ ] Alert detail screen.

## Phase 4: Weather Maps

- [ ] MapLibre map screen.
- [ ] RainViewer radar metadata fetch.
- [ ] Radar tile overlay.
- [ ] Timeline scrubber.
- [ ] Play/pause animation.
- [ ] Location marker.

## Phase 5: Android Platform Features

- [ ] Severe alert notifications.
- [ ] Daily weather summary notification.
- [ ] Small widget.
- [ ] Medium widget.
- [ ] Large widget.
- [ ] Widget configuration screen.

## Phase 6: Optional Advanced Features

- [ ] Rain starting/stopping notifications.
- [ ] Historical averages.
- [ ] More map layers.
- [ ] Multiple themes.
- [ ] Animated weather backgrounds.
- [ ] Tablet layout.
- [ ] Wear OS companion.

## Testing Backlog

- [ ] Open-Meteo DTO mapping tests.
- [ ] Weather condition mapping tests.
- [ ] Unit conversion tests.
- [ ] AQI category mapping tests.
- [ ] Alert severity mapping tests.
- [ ] Precipitation intensity mapping tests.
- [ ] Cache freshness logic tests.
- [ ] Notification dedupe logic tests.
- [ ] Moon phase calculation tests.
- [ ] Repository cache fallback tests.
- [ ] Saved location CRUD tests.
- [ ] Weather screen state UI tests.
- [ ] Search and save city UI tests.
