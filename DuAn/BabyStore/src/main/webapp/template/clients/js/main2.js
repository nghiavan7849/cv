$(document).ready(function() {
    // Initialize Isotope
    var $grid = $('.product-lists').isotope({
        itemSelector: '.element-item',
        layoutMode: 'fitRows'
    });

    // Filter items on button click
    $('.nav-item').on('click', function() {
        var filterValue = $(this).attr('data-filter');
        $grid.isotope({ filter: filterValue });
    });

    // Change is-checked class on buttons
    $('.cate-list li').each(function(i, buttonGroup) {
        var $buttonGroup = $(buttonGroup);
        $buttonGroup.on('click', 'button', function() {
            $buttonGroup.find('.is-checked').removeClass('is-checked');
            $(this).addClass('is-checked');
        });
    });
});