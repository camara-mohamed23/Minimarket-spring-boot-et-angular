import { api, headers } from './api.js';

const productsEl = document.getElementById('products');
const emptyEl = document.getElementById('productsEmpty');
const btnRefresh = document.getElementById('btnRefresh');

async function loadProducts(){
  try{
    const list = await api('/api/products');
    productsEl.innerHTML = '';
    if(!list || list.length === 0){ emptyEl.classList.remove('hidden'); return; }
    emptyEl.classList.add('hidden');

    list.forEach(p=>{
      const el = document.createElement('div');
      el.className = 'card';
      el.innerHTML = `
        <div class="thumb" style="background-image:url('${p.imageUrl || 'https://picsum.photos/seed/'+p.id+'/400/300'}')"></div>
        <div class="card-body">
          <div class="title">${p.name}</div>
          <div class="muted small">${p.description || ''}</div>
          <div class="price"><b>${Number(p.price).toFixed(2)} €</b></div>
          <div class="row">
            <input type="number" value="1" min="1" style="width:90px" data-qty>
            <button class="btn" data-add>Ajouter au panier</button>
            <span class="tag">ID: ${p.id}</span>
          </div>
        </div>
      `;
      el.querySelector('[data-add]').addEventListener('click', async ()=>{
        const qty = parseInt(el.querySelector('[data-qty]').value || '1',10);
        try{
          await api(`/api/cart/add/${p.id}?qty=${qty}`, { method:'POST', headers: headers(false) });
          alert('Ajouté au panier ✅');
        }catch(e){
          alert('Connecte-toi d’abord (login), puis réessaie.');
          location.href = 'login.html';
        }
      });
      productsEl.appendChild(el);
    });
  }catch(e){
    console.error(e);
    alert('Erreur lors du chargement des produits.');
  }
}

btnRefresh?.addEventListener('click', loadProducts);
loadProducts();
