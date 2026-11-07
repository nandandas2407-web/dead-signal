// Bridge between the SmartSpectra SDK and the game. NOT WIRED YET.
// Step 1: run the Presage Node/Electron starter and print one raw packet.
// Step 2: fix the field names in mapPacket() to match it (the names below are GUESSES).
// Step 3: call emit(mapPacket(raw)) from the starter's data callback inside start().
function mapPacket(raw){
  return {
    bpm: raw?.pulse?.rate,                 // GUESS: beats per minute
    breathingRate: raw?.breathing?.rate,   // GUESS: breaths per minute
    expressionShock: 0,                    // TODO: derive 0..1 from the SDK's expression output
    confidence: raw?.confidence ?? 1       // GUESS: 0..1
  };
}
function start(emit){
  const key = process.env.SMARTSPECTRA_API_KEY;
  if (!key) { console.log('[adapter] No API key set'); return; }
  // TODO: pass `key` to the SDK init call from the Presage starter.
  console.log('[adapter] SDK not wired: the game runs in SIM mode until emit() is called.');
}
module.exports={start,mapPacket};
