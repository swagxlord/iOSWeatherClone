# Android Weather App Clone Tasks

This tracker is derived from [docs/prd.md](prd.md). Keep the PRD as the source of truth for product requirements, and use this file to choose the next implementation slice.

## Current Focus

Phase 1 starts with a fake-data vertical slice:

- [x] Create project/module structure for core domain and weather feature work.
- [x] Add Navigation 3 app route keys and a Weather-root navigation host.
- [ ] Add app-owned weather domain models.
- [ ] Add a fake weather repository.
- [ ] Add MVI contracts for weather screen state, intents, and effects.
- [ ] Build the first Weather Detail screen in Jetpack Compose.
- [ ] Render current conditions, hourly forecast, 10-day forecast, and weather detail cards.
- [ ] Review the Weather Detail Compose implementation for component boundaries, state hoisting, accessibility, performance, and visual fidelity; then add loading placeholders and animated value transitions into loaded weather values.
- [ ] Keep provider DTOs out of UI-facing models.

## Phase 1: Core Forecast App

- [x] Project/module setup.
- [x] Navigation 3 setup with app-owned route keys, saved back stack, and Weather Detail as the app root.
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
- [ ] Animate weather values from loading/current values to newly loaded values, with cancellation and reduced-motion support.
- [ ] Local weather cache.

## Navigation Shape

The app should be organized around Weather Detail as the main/root destination:

```text
Weather Detail
├── Locations List
│   ├── Location Search
│   ├── Edit List
│   └── Settings/options menu
└── Weather Map
```

Secondary routes should exist only where they are opened from a primary screen or Android platform entry point:

- `Locations` opens as a full-screen departure from Weather Detail.
- `WeatherMap` opens as a full-screen departure from Weather Detail.
- `LocationSearch` opens from the Locations search affordance.
- `Settings` opens from the Locations overflow menu.
- `WidgetConfiguration` is reserved for Android widget setup.

The Weather Detail bottom navigation should show only on Weather Detail and should include a Weather Map button, a current/saved-location carousel indicator, and a Locations button.

## Phase 2: Visual Polish

- [ ] Weather-based backgrounds.
- [ ] Condition icon mapping.
- [ ] Temperature range bars.
- [ ] Swipe between saved locations.
- [ ] Better empty/error states.
- [ ] Unit settings.
- [ ] Smooth card animations.
- [ ] Review condition-background animation performance and accessibility on a physical device.

## Phase 3: Expanded Weather Data

- [ ] Open-Meteo Air Quality API.
- [ ] AQI card.
- [ ] AQI detail screen.
- [ ] Moon phase calculation.
- [ ] Better precipitation timeline.
- [ ] NWS severe alerts.

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
