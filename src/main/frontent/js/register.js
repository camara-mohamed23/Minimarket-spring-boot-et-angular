import { api, setToken } from './api.js';

const fullEl = document.getElementById('regFullname');
const emailEl = document.getElementById('regEmail');
const passEl = document.getElementById('regPassword');
const btnRegister = document.getElementById('btnRegister');

btnRegister.addEventListener('click', async ()=>{
  const fullName = fullEl.value.trim();
  const email = emailEl.value.trim();
  const password = passEl.value;
  if(!fullName || !email || !password){ alert('Tous les champs sont requis'); return; }

  try{
    const res = await api('/api/auth/register', {
      method:'POST',
      headers:{'Content-Type':'application/json'},
      body: JSON.stringify({ fullName, email, password })
    });
    setToken(res.token);
    alert('Compte créé ✅ connecté');
    location.href = 'index.html';
  }catch(e){
    console.error(e);
    alert('Erreur lors de la création du compte.');
  }
});
