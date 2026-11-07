const {app,BrowserWindow,session}=require('electron');const path=require('path');
const adapter=require('./src/sdk-adapter');
function createWindow(){
  const win=new BrowserWindow({width:1280,height:800,backgroundColor:'#050b08',webPreferences:{preload:path.join(__dirname,'preload.js'),contextIsolation:true,nodeIntegration:false}});
  win.loadFile('src/index.html');
  adapter.start(p=>win.webContents.send('signal',p));
}
app.whenReady().then(()=>{
  session.defaultSession.setPermissionRequestHandler((wc,perm,cb)=>cb(perm==='media'));
  createWindow();
});
app.on('window-all-closed',()=>app.quit());
