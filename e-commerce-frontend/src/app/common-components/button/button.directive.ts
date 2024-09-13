import { Directive, HostBinding } from '@angular/core';

@Directive({
    selector: '[primary-button]',
})
export class PrimaryButtonDirective {
    @HostBinding('class') get classes(): string {
        return 'button button--primary';
    }
}

@Directive({
    selector: '[secondary-button]',
})
export class SecondaryButtonDirective {
    @HostBinding('class') get classes(): string {
        return 'button button--secondary';
    }
}