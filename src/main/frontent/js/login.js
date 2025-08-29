import { api, setToken } from './api.js';

const emailEl = document.getElementById('logEmail');
const passEl = document.getElementById('logPassword');
const btnLogin = document.getElementById('btnLogin');

btnLogin.addEventListener('click', async ()=>{
  const email = emailEl.value.trim();
  const password = passEl.value;
  if(!email || !password){ alert('Email et mot de passe requis'); return; }

  try{
    const res = await api('/api/auth/login', {
      method:'POST',
      headers:{'Content-Type':'application/json'},
      body: JSON.stringify({ email, password })
    });
    setToken(res.token);
    alert('Connecté ✅');
    location.href = 'index.html';
  }catch(e){
    console.error(e);
    alert('Identifiants invalides.');
  }
});
