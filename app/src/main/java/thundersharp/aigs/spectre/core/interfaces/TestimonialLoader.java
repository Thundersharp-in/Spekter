package thundersharp.aigs.spectre.core.interfaces;

import java.util.List;

import thundersharp.aigs.spectre.core.models.Testimonials;

public interface TestimonialLoader {

    void OnTestimonialsLoaded(Testimonials... testimonials);
    void onDatabaseError(Exception e);

}
