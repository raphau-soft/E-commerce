import { Directive, HostBinding } from '@angular/core';

@Directive({
    selector: '[eInput]',
})
export class PrimaryInputDirective {
    @HostBinding('class') get classes(): string {
        return 'input';
    }
}