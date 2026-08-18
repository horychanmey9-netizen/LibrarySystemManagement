<script setup>
import ProfileCard from '@/components/user/ProfileCard.vue'

const profile = {
  fullName: 'John Doe',
  gender: 'Male',
  email: 'john.doe@gmail.com',
  address: 'Phnom Penh, Cambodia',
  phone: '+855 12 345 678',
  dateOfBirth: '2000-05-15',
  location: 'Phnom Penh, Cambodia',
  postalCode: '12000',
  avatar: 'https://i.pravatar.cc/150?img=12'
}

const saveProfile = (updatedProfile) => {
  console.log('Updated profile:', updatedProfile)

  // Later:
  // API update here
}
</script>

<template>
  <ProfileCard
    :profile="profile"
    @save="saveProfile"
  />
</template>