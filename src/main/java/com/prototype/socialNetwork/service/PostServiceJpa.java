package com.prototype.socialNetwork.service;

import com.prototype.socialNetwork.entity.Post;
import com.prototype.socialNetwork.entity.PostCategory;
import com.prototype.socialNetwork.entity.Profile;
import com.prototype.socialNetwork.repository.PostCategoryRepository;
import com.prototype.socialNetwork.repository.PostRepository;
import com.prototype.socialNetwork.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceJpa implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCategoryRepository postCategoryRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    /*

   public Choose insertChoose(ChooseRequest request) {

       // 1. Crear la Clave Compuesta
       ChooseId chooseId = new ChooseId(request.getProfileId(), request.getPreferenceId());

       // 2. Obtener Referencias Ligeras (Proxies) de las entidades
       // Esto es crucial para establecer las relaciones sin SELECTS innecesarios,
       // ya que la entidad Choose requiere las referencias a los objetos Profile y Preference.
       Profile profileRef = profileRepository.getReferenceById(request.getProfileId());
       Preference preferenceRef = preferenceRepository.getReferenceById(request.getPreferenceId());

       // 3. Crear y configurar la Entidad Choose
       Choose newChoose = new Choose();
       newChoose.setId(chooseId);           // Asigna la clave compuesta
       newChoose.setLevel(request.getLevel());

       // 4. Asignar las Entidades de Relación (FKs)
       // JPA utiliza estas referencias junto con @MapsId para mapear los IDs al guardar.
       newChoose.setProfile(profileRef);
       newChoose.setPreference(preferenceRef);

       // 5. Guardar: Si la combinación (profileId, preferenceId) ya existe, lanzará una excepción.
       return chooseRepository.save(newChoose);
   }

    */
    @Transactional
    @Override
    public Post insertPost(String title, String body, Integer profileId, Integer postCategoryId, String imageUrl) {
        Post post = new Post();
        Profile profile = profileRepository.getReferenceById(profileId);
        PostCategory postCategory = postCategoryRepository.getReferenceById(postCategoryId);
        post.setPostTitle(title);
        post.setPostBody(body);
        post.setPostDate(LocalDateTime.now());
        post.setProfile(profile);
        post.setCategory(postCategory);
        post.setImageUrl(imageUrl);
        return postRepository.save(post);
    }
}
