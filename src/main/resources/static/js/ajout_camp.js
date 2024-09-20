// document.getElementById('toggleInput').addEventListener('change', function() {
//     var inputContainer = document.getElementById('inputContainer');
//     if(this.checked) {
//         inputContainer.style.display = 'block';
//     } else {
//         inputContainer.style.display = 'none';
//     }
// });



document.addEventListener('DOMContentLoaded', function() {
    var selectElement = document.getElementById('type');
    var dynamicField = document.getElementById('url');

    selectElement.addEventListener('change', function() {
            var selectedOption = selectElement.options[selectElement.selectedIndex]; // recupere l'option selectionner 
            var selectedType = selectedOption.text; // Récupère le texte sélectionnée

            console.log('Selected type:[' + selectedType + "]" );


            dynamicField.innerHTML = ''; // Vider le contenu actuel

            if (selectedType === 'Youtube') {
                // Insérer le champ URL Vidéo
                dynamicField.innerHTML = `
                    <div class="form-group">
                        <label for="urlVideo">URL Vidéo Youtube</label>
                        <textarea class="form-control" id="urlVideo" name="urlVideo" th:field="*{urlVideo}" rows="3" required></textarea>
                    </div>
                `;
            } else if (selectedType === 'Publication') {
                // Insérer le champ Fichier
                dynamicField.innerHTML = `
                    <div class="form-group">
                        <label for="videoLocal">Sélectionner le fichier vidéo</label>
                        <input type="file" id="videoLocal" name="videoLocal"  th:field="*{videoLocal}" accept="video/*"  required/>
                    </div>
                `;
            } else if(selectedType === 'Carousel') {
                // Insérer le champ multiple Fichier
                console.log("miditra")
                dynamicField.innerHTML = `
                    <div class="form-group">
                    <label for="img_carousel">Sélectionner les fichiers images pour la carousel</label>
                    <input type="file" id="img_carousel" name="img_carousel[]"  th:field="*{img_carousel}" accept="image/*" multiple required/>
                    </div>
                `;
            }

        });
        
    });
