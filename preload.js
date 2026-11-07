const {contextBridge,ipcRenderer}=require('electron');
contextBridge.exposeInMainWorld('bridge',{onSignal:f=>ipcRenderer.on('signal',(_,p)=>f(p))});
