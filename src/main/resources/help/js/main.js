console.log("IUH Pharmacity guide loaded");

// Hàm tìm kiếm câu hỏi
function searchQuestions(query) {
  const searchResults = document.getElementById('search-results');
  const cardsContainer = document.querySelector('.cards-section .cards-container');
  
  if (!query || query.trim() === '') {
    searchResults.innerHTML = '';
    searchResults.style.display = 'none';
    // Hiện lại các thẻ gốc
    document.querySelectorAll('.section-title, .row.g-4').forEach(el => {
      el.style.display = '';
    });
    return;
  }

  // Ẩn các thẻ gốc
  document.querySelectorAll('.section-title, .row.g-4').forEach(el => {
    if (!el.id) {
      el.style.display = 'none';
    }
  });

  const normalizedQuery = query.toLowerCase().trim();
  const results = [];

  // Tìm kiếm trong questionsData
  for (const category in questionsData) {
    const questions = questionsData[category];
    questions.forEach(item => {
      const questionText = item.question.toLowerCase();
      if (questionText.includes(normalizedQuery)) {
        results.push({
          category: category,
          question: item.question,
          answer: item.answer
        });
      }
    });
  }

  // Hiển thị kết quả
  if (results.length === 0) {
    searchResults.innerHTML = `
      <div class="col-12 text-center py-5">
        <i class="fas fa-search fa-3x text-muted mb-3"></i>
        <h4 class="text-muted">Không tìm thấy câu hỏi nào phù hợp</h4>
        <p class="text-muted">Hãy thử tìm kiếm với từ khóa khác</p>
      </div>
    `;
  } else {
    searchResults.innerHTML = `
      <div class="col-12">
        <h3 class="section-title">Kết quả tìm kiếm (${results.length} câu hỏi)</h3>
      </div>
      ${results.map(result => `
        <div class="col-12 col-md-6">
          <div class="search-result-card" data-category="${result.category}" data-question="${encodeURIComponent(result.question)}">
            <div class="question-badge">
              <i class="fas fa-question-circle"></i> ${result.category}
            </div>
            <h5 class="question-title">${result.question}</h5>
            <div class="question-preview">
              ${result.answer[0].type === 'text' ? result.answer[0].content : 'Xem chi tiết để biết thêm...'}
            </div>
            <button class="btn-view-detail">
              <i class="fas fa-arrow-right"></i> Xem chi tiết
            </button>
          </div>
        </div>
      `).join('')}
    `;

    // Thêm sự kiện click cho các nút xem chi tiết
    document.querySelectorAll('.btn-view-detail').forEach(btn => {
      btn.addEventListener('click', function() {
        const card = this.closest('.search-result-card');
        const category = card.dataset.category;
        const question = decodeURIComponent(card.dataset.question);
        window.location.href = `html/details.html?name=${encodeURIComponent(category)}&question=${encodeURIComponent(question)}`;
      });
    });
  }

  searchResults.style.display = 'flex';
}

// Xử lý sự kiện tìm kiếm
document.addEventListener('DOMContentLoaded', function() {
  const searchInput = document.querySelector('.search-input');
  
  if (searchInput) {
    // Tìm kiếm khi người dùng gõ (debounce)
    let searchTimeout;
    searchInput.addEventListener('input', function(e) {
      clearTimeout(searchTimeout);
      searchTimeout = setTimeout(() => {
        searchQuestions(e.target.value);
      }, 300);
    });

    // Tìm kiếm khi nhấn Enter
    searchInput.addEventListener('keypress', function(e) {
      if (e.key === 'Enter') {
        clearTimeout(searchTimeout);
        searchQuestions(e.target.value);
      }
    });
  }
});
