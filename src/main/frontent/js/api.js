// ------------ CONFIG ------------
export const BASE_URL = 'http://localhost:8180'; // adapte le port si besoin

// afficher la base URL en bas de page (si tag présent)
const apiTag = document.getElementById('apiUrlTag');
if (apiTag) apiTag.textContent = BASE_URL;

// ------------ JWT STORAGE ------------
export function getToken(){ return localStorage.getItem('jwt'); }
export function setToken(t){ if(t) localStorage.setItem('jwt', t); else localStorage.removeItem('jwt'); }

// Décodage simple du payload (pour UI, non-sécurisé)
export function decodeJwt(token){
  try{
    const payload = token.split('.')[1];
    const json = atob(payload.replace(/-/g,'+').replace(/_/g,'/'));
    return JSON.parse(json);
  }catch(e){ return null; }
}

// Headers
export function headers(json=true){
  const h = {};
  if(json) h['Content-Type'] = 'application/json';
  const tok = getToken();
  if(tok) h['Authorization'] = 'Bearer '+tok;
  return h;
}

// Fetch wrapper
export async function api(path, options={}){
  const res = await fetch(BASE_URL+path, options);
  if(!res.ok){
    const txt = await res.text();
    throw new Error(txt || res.statusText);
  }
  const ct = res.headers.get('content-type')||'';
  if(ct.includes('application/json')) return res.json();
  return res.text();
}
