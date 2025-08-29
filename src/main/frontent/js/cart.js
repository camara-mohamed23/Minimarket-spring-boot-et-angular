import { api, headers, getToken } from './api.js';

const cartList = document.getElementById('cartList');
const cartEmpty = document.getElementById('cartEmpty');
const ordersList = document.getElementById('ordersList');
const ordersEmpty = document.getElementById('ordersEmpty');
const btnCheckout = document.getElementById('btnCheckout');

async function loadCart(){
  if(!getToken()){ cartList.innerHTML=''; cartEmpty.classList.remove('hidden'); return; }
  try{
    const cart = await api('/api/cart', { headers: headers(false) });
    cartList.innerHTML = '';
    if(!cart || !cart.items || cart.items.length===0){ cartEmpty.classList.remove('hidden'); return; }
    cartEmpty.classList.add('hidden');

    cart.items.forEach(it=>{
      const line = document.createElement('div');
      line.className = 'item';
      line.innerHTML = `
        <div>
          <div><b>${it.product?.name || 'Produit'}</b> <span class="muted small">#${it.product?.id}</span></div>
          <div class="muted small">PU: ${Number(it.unitPrice).toFixed(2)} €</div>
        </div>
        <div class="row">
          <input type="number" value="${it.quantity}" min="1" style="width:90px" data-qty>
          <button class="btn" data-upd>MAJ</button>
          <button class="btn danger" data-del>Suppr</button>
        </div>
      `;
      line.querySelector('[data-upd]').addEventListener('click', async ()=>{
        const qty = parseInt(line.querySelector('[data-qty]').value||'1',10);
        await api(`/api/cart/qty/${it.product.id}?qty=${qty}`, { method:'PUT', headers: headers(false) });
        await loadCart();
      });
      line.querySelector('[data-del]').addEventListener('click', async ()=>{
        await api(`/api/cart/remove/${it.product.id}`, { method:'DELETE', headers: headers(false) });
        await loadCart();
      });
      cartList.appendChild(line);
    });
  }catch(e){
    console.error(e);
    cartList.innerHTML=''; cartEmpty.classList.remove('hidden');
  }
}

async function checkout(){
  if(!getToken()){ alert('Connecte-toi'); return; }
  try{
    const order = await api('/api/orders/checkout', { method:'POST', headers: headers(false) });
    alert(`Commande #${order.id} – ${order.status}`);
    await loadCart();
    await loadOrders();
  }catch(e){
    console.error(e);
    alert('Paiement échoué / erreur.');
  }
}

async function loadOrders(){
  if(!getToken()){ ordersList.innerHTML=''; ordersEmpty.classList.remove('hidden'); return; }
  try{
    const list = await api('/api/orders', { headers: headers(false) });
    ordersList.innerHTML = '';
    if(!list || list.length===0){ ordersEmpty.classList.remove('hidden'); return; }
    ordersEmpty.classList.add('hidden');

    list.forEach(o=>{
      const box = document.createElement('div');
      box.className = 'item';
      const total = Number(o.totalAmount||0).toFixed(2);
      box.innerHTML = `
        <div>
          <div><b>Commande #${o.id}</b> <span class="tag">${o.status}</span></div>
          <div class="muted small">${new Date(o.createdAt || Date.now()).toLocaleString()}</div>
          <div class="muted small">Total: ${total} €</div>
        </div>
        <div class="small">${(o.items||[]).length} article(s)</div>
      `;
      ordersList.appendChild(box);
    });
  }catch(e){
    console.error(e);
    ordersList.innerHTML=''; ordersEmpty.classList.remove('hidden');
  }
}

btnCheckout.addEventListener('click', checkout);
loadCart();
loadOrders();
