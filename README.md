# Geyser - Split Screen Guest Support

Fork of [GeyserMC/Geyser](https://github.com/GeyserMC/Geyser) adding split screen guest support for console subclients (Xbox, PlayStation, Switch).

## Feature Branch

All split screen work lives on [`feature/splitscreen-guest-support`](../../tree/feature/splitscreen-guest-support).

Split screen guests connect to the Java server as offline-mode players with names and UUIDs derived from the primary player's session (e.g., `Player_Guest1`).

## Downloads

Pre-built JARs for all platforms are available on the [Releases page](../../releases). The latest release is automatically built from the feature branch.

Available artifacts: Standalone, Spigot, Fabric, NeoForge, BungeeCord, Velocity, ViaProxy.

## CI

This fork uses an orphan `ci` branch (this branch) for CI workflows, keeping the feature branch clean:

- **Build & Release** - Builds all platform JARs and publishes a GitHub Release on every push to the feature branch.
- **Auto Rebase** - Weekly (Mondays), checks that upstream master is green, then rebases the feature branch onto it. Opens an issue if conflicts arise.

## Upstream

Based on Geyser `master`. The feature branch is kept up to date via automated weekly rebases.
