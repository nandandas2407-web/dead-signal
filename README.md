# DEAD_SIGNAL
A terminal stealth game where your real pulse, breathing and expressions (Presage SmartSpectra SDK) drive the trace meter. Calm down to survive the ICE sweep. Entry for the DEV Presage SmartSpectra SDK Challenge, Most Creative or Delightful Use.

## Run
```
npm install
npm start
```
Without the SDK wired in, the game runs on SIM data (labelled SIM on screen). **Record the demo only in `[SDK LIVE]` mode.**

## Wire the SDK
Edit `src/sdk-adapter.js`: print a raw packet from the Presage starter, fix `mapPacket()`, call `emit()`. Set your API key via env var, never commit it. If the SDK needs exclusive camera access, turn off the in-game camera preview.

## How to play
Calibrate 12s, type the hex payloads, survive the sweeps. Trace rises faster as your pulse rises above baseline. At 100% the keyboard locks: keep your face neutral and slow your breathing to drain it. Keys: ESC wipe, P pause. Difficulty is chosen at boot.

## Features
Baseline-relative scoring, time-based asymmetric smoothing, confidence gating, outlier clamp, flinch debounce, a heartbeat that thumps at your measured BPM, a live raw-vs-smoothed sparkline, a full-run replay graph on flatline, and an optional JSON export of your own run.

## Consent and data handling (verify every line against the SDK telemetry page before submitting)
- Camera starts only after you click it. Content warning shown first (sudden sound, flashes).
- This app stores nothing on disk and makes no network calls of its own. Run data lives in RAM until you press ESC or quit. Export is manual.
- SDK processing, SDK telemetry and retention: **TODO, fill in from Presage docs.**
- No AI services used. Not a medical tool: no diagnosis, no health claims.
