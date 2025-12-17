
const params = new URLSearchParams(window.location.search);
const featureName = params.get("name");
const questionParam = params.get("question");
const featureData = questionsData[featureName];


if (featureData) {
  document.getElementById("feature-title").textContent = featureName;
  const accordion = document.getElementById("accordionExample");
  
  featureData.forEach((item, index) => {
    const answerContent = item.answer
      .map((part) => {
        if (part.type === "text") {
          return `<p>${part.content}</p>`;
        } else if (part.type === "image") {
          return `<img src="${part.content}" alt="Mô tả ảnh" style="max-width: 100%; height: auto;" />`;
        }
        return "";
      })
      .join("");
    
    // Kiểm tra xem có phải câu hỏi được chọn từ tìm kiếm không
    const isExpanded = questionParam && item.question === questionParam;
    const collapseClass = isExpanded ? 'collapse show' : 'collapse';
    const buttonClass = isExpanded ? 'accordion-button' : 'accordion-button collapsed';
    const ariaExpanded = isExpanded ? 'true' : 'false';
    
    accordion.innerHTML += `
      <div class="accordion-item my-2">
        <h2 class="accordion-header" id="heading${index}">
          <button
            class="${buttonClass}" 
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#collapse${index}"
            aria-expanded="${ariaExpanded}"
            aria-controls="collapse${index}" 
          >
          ${item.question}
          </button>
        </h2>
        <div
          id="collapse${index}"
          class="accordion-collapse ${collapseClass}"
          aria-labelledby="heading${index}"
          data-bs-parent="#accordionExample"
        >
          <div class="accordion-body">
            ${answerContent}
          </div>
        </div>
      </div>
    `;
  });

  // Cuộn đến câu hỏi được chọn nếu có
  if (questionParam) {
    setTimeout(() => {
      const selectedIndex = featureData.findIndex(item => item.question === questionParam);
      if (selectedIndex !== -1) {
        const element = document.getElementById(`heading${selectedIndex}`);
        if (element) {
          element.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
      }
    }, 300);
  }
} else {
  document.getElementById("feature-title").textContent =
    "Không tìm thấy nội dung!";
}